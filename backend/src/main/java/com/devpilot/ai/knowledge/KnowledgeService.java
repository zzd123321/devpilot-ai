package com.devpilot.ai.knowledge;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class KnowledgeService {

    private final KnowledgeRepository knowledgeRepository;
    private final KnowledgeDocumentRepository documentRepository;

    // Service 依赖的是我们自己定义的 Repository 接口，而不是直接依赖 Spring Data JPA。
    // 这样以后从 H2/PostgreSQL 换成别的存储方案时，业务层改动会更小。
    public KnowledgeService(
            KnowledgeRepository knowledgeRepository,
            KnowledgeDocumentRepository documentRepository
    ) {
        this.knowledgeRepository = knowledgeRepository;
        this.documentRepository = documentRepository;
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

        return new AskKnowledgeResponse(
                "This is a placeholder answer for knowledge base `%s`: %s"
                        .formatted(knowledgeBase.id(), question),
                List.of(new SourceReference("README.md", "Project overview", 0.92))
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

    // 上传文档会做两次写入：保存文档、更新知识库文档数量。
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
                Instant.now()
        );

        KnowledgeDocument savedDocument = documentRepository.save(document);
        knowledgeRepository.incrementDocumentCount(knowledgeBaseId);
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
                document.createdAt()
        );
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
}
