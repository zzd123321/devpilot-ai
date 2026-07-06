package com.devpilot.ai.knowledge;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "knowledge_documents")
public class KnowledgeDocumentEntity {

    // 文档实体暂时保存原始文本内容；下一阶段会基于 content 做 chunk 切分。
    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 36)
    private String knowledgeBaseId;

    @Column(nullable = false, length = 255)
    private String filename;

    @Column(length = 120)
    private String contentType;

    @Column(nullable = false)
    private long sizeBytes;

    // columnDefinition = "text" 用来和 Flyway 脚本中的 content text 对齐，避免 Hibernate 校验失败。
    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @Column(nullable = false)
    private Instant createdAt;

    protected KnowledgeDocumentEntity() {
    }

    // JPA Entity 用普通 class，而不是 record，因为 JPA 需要反射和无参构造函数。
    public KnowledgeDocumentEntity(
            String id,
            String knowledgeBaseId,
            String filename,
            String contentType,
            long sizeBytes,
            String content,
            Instant createdAt
    ) {
        this.id = id;
        this.knowledgeBaseId = knowledgeBaseId;
        this.filename = filename;
        this.contentType = contentType;
        this.sizeBytes = sizeBytes;
        this.content = content;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getKnowledgeBaseId() {
        return knowledgeBaseId;
    }

    public String getFilename() {
        return filename;
    }

    public String getContentType() {
        return contentType;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public String getContent() {
        return content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
