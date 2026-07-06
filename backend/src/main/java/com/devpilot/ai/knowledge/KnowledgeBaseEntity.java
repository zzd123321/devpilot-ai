package com.devpilot.ai.knowledge;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "knowledge_bases")
public class KnowledgeBaseEntity {

    // Entity 对应数据库表结构；字段约束要和 Flyway SQL 保持一致。
    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(nullable = false)
    private long documentCount;

    @Column(nullable = false)
    private Instant createdAt;

    protected KnowledgeBaseEntity() {
    }

    // JPA 需要无参构造函数；业务代码创建实体时使用这个有参构造函数。
    public KnowledgeBaseEntity(String id, String name, long documentCount, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.documentCount = documentCount;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getDocumentCount() {
        return documentCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void incrementDocumentCount() {
        this.documentCount++;
    }
}
