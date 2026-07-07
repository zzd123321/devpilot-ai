package com.devpilot.ai.knowledge;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class KnowledgeService {

    private final KnowledgeRepository knowledgeRepository;
    private final KnowledgeDocumentRepository documentRepository;
    private final DocumentChunkRepository chunkRepository;
    private final TextChunker textChunker;

    // Service 依赖的是我们自己定义的 Repository 接口，而不是直接依赖 Spring Data JPA。
    // 这样以后从 H2/PostgreSQL 换成别的存储方案时，业务层改动会更小。
    public KnowledgeService(
            KnowledgeRepository knowledgeRepository,
            KnowledgeDocumentRepository documentRepository,
            DocumentChunkRepository chunkRepository,
            TextChunker textChunker
    ) {
        this.knowledgeRepository = knowledgeRepository;
        this.documentRepository = documentRepository;
        this.chunkRepository = chunkRepository;
        this.textChunker = textChunker;
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
                    List.of()
            );
        }

        List<SourceReference> sources = scoredChunks.stream()
                .map(this::toSourceReference)
                .toList();

        return new AskKnowledgeResponse(
                "我先用关键词检索找到了 %d 个相关片段。下一步接入 AI 后，会把这些片段作为上下文生成自然语言回答。"
                        .formatted(sources.size()),
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

    // 上传文档会做多次写入：保存文档、保存 chunk、更新知识库文档数量。
    // @Transactional 保证它们要么都成功，要么都回滚，避免数据不一致。
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

        // 先保存 PROCESSING 状态。后续改成异步任务时，前端就能看到“处理中”的文档。
        KnowledgeDocument savedDocument = documentRepository.save(document);
        List<DocumentChunk> chunks = buildChunks(savedDocument);
        chunkRepository.saveAll(chunks);
        KnowledgeDocument readyDocument = documentRepository.save(markDocumentReady(savedDocument));
        knowledgeRepository.incrementDocumentCount(knowledgeBaseId);
        return toDocumentSummary(readyDocument);
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

    private KnowledgeDocument markDocumentReady(KnowledgeDocument document) {
        return new KnowledgeDocument(
                document.id(),
                document.knowledgeBaseId(),
                document.filename(),
                document.contentType(),
                document.sizeBytes(),
                document.content(),
                DocumentProcessingStatus.READY,
                null,
                document.createdAt()
        );
    }

    private List<DocumentChunk> buildChunks(KnowledgeDocument document) {
        return textChunker.split(document.content())
                .stream()
                .map(chunk -> new DocumentChunk(
                        UUID.randomUUID().toString(),
                        document.knowledgeBaseId(),
                        document.id(),
                        chunk.index(),
                        chunk.content(),
                        chunk.charStart(),
                        chunk.charEnd(),
                        Instant.now()
                ))
                .toList();
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

    private double scoreChunk(DocumentChunk chunk, Set<String> terms) {
        String content = chunk.content().toLowerCase(Locale.ROOT);
        double score = 0;

        for (String term : terms) {
            if (content.contains(term)) {
                score += Math.min(term.length(), 8);
            }
        }

        return score;
    }

    private SourceReference toSourceReference(ScoredChunk scoredChunk) {
        DocumentChunk chunk = scoredChunk.chunk();
        String documentName = documentRepository.findById(chunk.documentId())
                .map(KnowledgeDocument::filename)
                .orElse("Unknown document");

        return new SourceReference(
                documentName,
                snippet(chunk.content()),
                scoredChunk.score()
        );
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

    private record ScoredChunk(DocumentChunk chunk, double score) {
    }
}
