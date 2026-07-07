package com.devpilot.ai.knowledge;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

@Service
public class KnowledgeService {

    private final KnowledgeRepository knowledgeRepository;
    private final KnowledgeDocumentRepository documentRepository;
    private final DocumentChunkRepository chunkRepository;
    private final DocumentProcessingService documentProcessingService;

    // Service 依赖的是我们自己定义的 Repository 接口，而不是直接依赖 Spring Data JPA。
    // 这样以后从 H2/PostgreSQL 换成别的存储方案时，业务层改动会更小。
    public KnowledgeService(
            KnowledgeRepository knowledgeRepository,
            KnowledgeDocumentRepository documentRepository,
            DocumentChunkRepository chunkRepository,
            DocumentProcessingService documentProcessingService
    ) {
        this.knowledgeRepository = knowledgeRepository;
        this.documentRepository = documentRepository;
        this.chunkRepository = chunkRepository;
        this.documentProcessingService = documentProcessingService;
    }

    public List<KnowledgeBaseSummary> listKnowledgeBases() {
        // Repository 返回领域对象，Service 再转换成前端需要的 DTO。
        return knowledgeRepository.findAll()
                .stream()
                .map(this::toSummary)
                .toList();
    }

    public KnowledgeBaseSummary createKnowledgeBase(String name) {
        // ID 在后端生成，前端只负责传名称；这样可以统一控制业务主键规则。
        KnowledgeBase knowledgeBase = new KnowledgeBase(
                UUID.randomUUID().toString(),
                name.trim(),
                0
        );
        return toSummary(knowledgeRepository.save(knowledgeBase));
    }

    public AskKnowledgeResponse ask(String knowledgeBaseId, String question) {
        // 先确认知识库存在。不存在就抛业务异常，由 GlobalExceptionHandler 转成 404。
        KnowledgeBase knowledgeBase = knowledgeRepository.findById(knowledgeBaseId)
                .orElseThrow(() -> new KnowledgeBaseNotFoundException(knowledgeBaseId));
        List<ScoredChunk> scoredChunks = retrieveRelevantChunks(knowledgeBase.id(), question);

        if (scoredChunks.isEmpty()) {
            return new AskKnowledgeResponse(
                    "当前知识库还没有检索到相关片段。你可以先上传 Markdown 或文本文件，再重新提问。",
                    "",
                    List.of()
            );
        }

        List<SourceReference> sources = scoredChunks.stream()
                .map(this::toSourceReference)
                .toList();
        String promptPreview = buildPromptPreview(question, scoredChunks);

        return new AskKnowledgeResponse(
                "我先用关键词检索找到了 %d 个相关片段。下一步接入 AI 后，会把这些片段作为上下文生成自然语言回答。"
                        .formatted(sources.size()),
                promptPreview,
                sources
        );
    }

    public List<KnowledgeDocumentSummary> listDocuments(String knowledgeBaseId) {
        // 查询子资源前也要校验父资源是否存在，避免返回含糊的空列表。
        ensureKnowledgeBaseExists(knowledgeBaseId);
        return documentRepository.findAllByKnowledgeBaseId(knowledgeBaseId)
                .stream()
                .map(this::toDocumentSummary)
                .toList();
    }

    public List<DocumentChunkSummary> listDocumentChunks(String knowledgeBaseId, String documentId) {
        ensureKnowledgeBaseExists(knowledgeBaseId);
        KnowledgeDocument document = documentRepository.findById(documentId)
                .filter(item -> item.knowledgeBaseId().equals(knowledgeBaseId))
                .orElseThrow(() -> new KnowledgeDocumentNotFoundException(documentId));

        // PROCESSING 或 FAILED 文档可能还没有 chunks，直接返回空列表即可。
        return chunkRepository.findAllByDocumentId(document.id())
                .stream()
                .map(this::toChunkSummary)
                .toList();
    }

    // 上传接口只负责保存原始文档和标记 PROCESSING，耗时的切分逻辑交给后台线程。
    @Transactional
    public KnowledgeDocumentSummary uploadDocument(String knowledgeBaseId, MultipartFile file) {
        ensureKnowledgeBaseExists(knowledgeBaseId);
        validateDocument(file);

        KnowledgeDocument document = new KnowledgeDocument(
                UUID.randomUUID().toString(),
                knowledgeBaseId,
                cleanFilename(file.getOriginalFilename()),
                file.getContentType(),
                file.getSize(),
                readFileContent(file),
                DocumentProcessingStatus.PROCESSING,
                null,
                Instant.now()
        );

        KnowledgeDocument savedDocument = documentRepository.save(document);
        knowledgeRepository.incrementDocumentCount(knowledgeBaseId);
        dispatchDocumentProcessingAfterCommit(savedDocument.id());
        return toDocumentSummary(savedDocument);
    }

    private void ensureKnowledgeBaseExists(String knowledgeBaseId) {
        knowledgeRepository.findById(knowledgeBaseId)
                .orElseThrow(() -> new KnowledgeBaseNotFoundException(knowledgeBaseId));
    }

    private KnowledgeBaseSummary toSummary(KnowledgeBase knowledgeBase) {
        return new KnowledgeBaseSummary(
                knowledgeBase.id(),
                knowledgeBase.name(),
                knowledgeBase.documentCount()
        );
    }

    private KnowledgeDocumentSummary toDocumentSummary(KnowledgeDocument document) {
        return new KnowledgeDocumentSummary(
                document.id(),
                document.filename(),
                document.contentType(),
                document.sizeBytes(),
                document.processingStatus(),
                document.processingError(),
                document.createdAt(),
                chunkRepository.countByDocumentId(document.id())
        );
    }

    private DocumentChunkSummary toChunkSummary(DocumentChunk chunk) {
        return new DocumentChunkSummary(
                chunk.id(),
                chunk.chunkIndex(),
                chunk.content(),
                chunk.charStart(),
                chunk.charEnd()
        );
    }

    private List<ScoredChunk> retrieveRelevantChunks(String knowledgeBaseId, String question) {
        Set<String> terms = extractSearchTerms(question);
        if (terms.isEmpty()) {
            return List.of();
        }

        return chunkRepository.findAllByKnowledgeBaseId(knowledgeBaseId)
                .stream()
                .map(chunk -> new ScoredChunk(chunk, scoreChunk(chunk, terms)))
                .filter(scoredChunk -> scoredChunk.score() > 0)
                .sorted(Comparator.comparingDouble(ScoredChunk::score).reversed())
                .limit(5)
                .toList();
    }

    private Set<String> extractSearchTerms(String question) {
        String normalizedQuestion = question.toLowerCase(Locale.ROOT).trim();
        Set<String> terms = new LinkedHashSet<>();

        for (String term : normalizedQuestion.split("[^\\p{IsAlphabetic}\\p{IsDigit}\\p{IsHan}]+")) {
            if (term.length() >= 2) {
                terms.add(term);
            }
        }

        // 中文问题常常没有空格，额外加入相邻双字词，让简单关键词检索也能命中一部分中文内容。
        for (int index = 0; index < normalizedQuestion.length() - 1; index++) {
            String term = normalizedQuestion.substring(index, index + 2).trim();
            if (term.codePoints().allMatch(Character::isLetter)) {
                terms.add(term);
            }
        }

        return terms;
    }

    private void dispatchDocumentProcessingAfterCommit(String documentId) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            documentProcessingService.processDocument(documentId);
            return;
        }

        // 等上传事务真正提交后再启动后台线程，避免后台线程读不到刚保存的文档。
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                documentProcessingService.processDocument(documentId);
            }
        });
    }

    private ChunkScore scoreChunk(DocumentChunk chunk, Set<String> terms) {
        String content = chunk.content().toLowerCase(Locale.ROOT);
        double score = 0;
        List<String> matchedTerms = new ArrayList<>();

        for (String term : terms) {
            if (content.contains(term)) {
                score += Math.min(term.length(), 8);
                matchedTerms.add(term);
            }
        }

        return new ChunkScore(score, matchedTerms);
    }

    private SourceReference toSourceReference(ScoredChunk scoredChunk) {
        DocumentChunk chunk = scoredChunk.chunk();
        String documentName = documentRepository.findById(chunk.documentId())
                .map(KnowledgeDocument::filename)
                .orElse("Unknown document");

        return new SourceReference(
                chunk.documentId(),
                documentName,
                chunk.id(),
                chunk.chunkIndex(),
                chunk.charStart(),
                chunk.charEnd(),
                snippet(chunk.content()),
                scoredChunk.score(),
                scoredChunk.matchedTerms()
        );
    }

    private String buildPromptPreview(String question, List<ScoredChunk> scoredChunks) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是 DevPilot AI，一个严谨的项目知识库助手。\n");
        prompt.append("请只根据【参考资料】回答【用户问题】。如果资料不足，请明确说明不知道，不要编造。\n\n");
        prompt.append("【用户问题】\n");
        prompt.append(question).append("\n\n");
        prompt.append("【参考资料】\n");

        for (int index = 0; index < scoredChunks.size(); index++) {
            DocumentChunk chunk = scoredChunks.get(index).chunk();
            String documentName = documentRepository.findById(chunk.documentId())
                    .map(KnowledgeDocument::filename)
                    .orElse("Unknown document");

            prompt.append("资料 ").append(index + 1)
                    .append("：")
                    .append(documentName)
                    .append(" / Chunk #")
                    .append(chunk.chunkIndex() + 1)
                    .append("\n");
            prompt.append(chunk.content()).append("\n\n");
        }

        prompt.append("【回答要求】\n");
        prompt.append("1. 先给出直接回答。\n");
        prompt.append("2. 如果引用了资料，请说明来自哪个文档或 chunk。\n");
        prompt.append("3. 如果资料不足，请列出还需要补充什么信息。\n");
        return prompt.toString();
    }

    private String snippet(String content) {
        String singleLineContent = content.replace("\n", " ").trim();
        if (singleLineContent.length() <= 180) {
            return singleLineContent;
        }
        return singleLineContent.substring(0, 180) + "...";
    }

    private void validateDocument(MultipartFile file) {
        // 后端校验不能省。前端 accept 只是体验优化，用户仍然可以绕过页面直接调接口。
        if (file.isEmpty()) {
            throw new InvalidDocumentException("Document file is required.");
        }

        if (file.getSize() > 1024 * 1024) {
            throw new InvalidDocumentException("Document must be 1 MB or smaller.");
        }

        String filename = cleanFilename(file.getOriginalFilename()).toLowerCase();
        if (!filename.endsWith(".txt") && !filename.endsWith(".md") && !filename.endsWith(".markdown")) {
            throw new InvalidDocumentException("Only .txt, .md, and .markdown files are supported for now.");
        }
    }

    private String cleanFilename(String filename) {
        if (filename == null || filename.isBlank()) {
            return "untitled.txt";
        }
        // 有些浏览器或客户端可能传入带路径的文件名，这里只保留最后的文件名部分。
        String normalizedFilename = filename.replace("\\", "/");
        return normalizedFilename.substring(normalizedFilename.lastIndexOf("/") + 1);
    }

    private String readFileContent(MultipartFile file) {
        try {
            // 当前阶段只支持文本文件，所以先按 UTF-8 读取；PDF/Word 后续需要专门解析器。
            return new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new InvalidDocumentException("Could not read document content.");
        }
    }

    private record ChunkScore(double score, List<String> matchedTerms) {
    }

    private record ScoredChunk(DocumentChunk chunk, ChunkScore chunkScore) {
        private double score() {
            return chunkScore.score();
        }

        private List<String> matchedTerms() {
            return chunkScore.matchedTerms();
        }
    }
}
