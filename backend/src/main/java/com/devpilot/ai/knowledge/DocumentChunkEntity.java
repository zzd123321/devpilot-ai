package com.devpilot.ai.knowledge;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "document_chunks")
public class DocumentChunkEntity {

    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 36)
    private String knowledgeBaseId;

    @Column(nullable = false, length = 36)
    private String documentId;

    @Column(nullable = false)
    private int chunkIndex;

    // chunk 内容后续会用于 embedding 和相似度检索，因此这里也和 Flyway 的 text 类型保持一致。
    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @Column(nullable = false)
    private int charStart;

    @Column(nullable = false)
    private int charEnd;

    @Column(nullable = false)
    private Instant createdAt;

    protected DocumentChunkEntity() {
    }

    public DocumentChunkEntity(
            String id,
            String knowledgeBaseId,
            String documentId,
            int chunkIndex,
            String content,
            int charStart,
            int charEnd,
            Instant createdAt
    ) {
        this.id = id;
        this.knowledgeBaseId = knowledgeBaseId;
        this.documentId = documentId;
        this.chunkIndex = chunkIndex;
        this.content = content;
        this.charStart = charStart;
        this.charEnd = charEnd;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getKnowledgeBaseId() {
        return knowledgeBaseId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public int getChunkIndex() {
        return chunkIndex;
    }

    public String getContent() {
        return content;
    }

    public int getCharStart() {
        return charStart;
    }

    public int getCharEnd() {
        return charEnd;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

