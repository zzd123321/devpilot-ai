package com.devpilot.ai.knowledge;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "ask_records")
public class AskRecordEntity {

    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 36)
    private String knowledgeBaseId;

    @Column(nullable = false, columnDefinition = "text")
    private String question;

    @Column(nullable = false, columnDefinition = "text")
    private String answer;

    @Column(nullable = false, length = 80)
    private String answerProvider;

    @Column(nullable = false)
    private int sourceCount;

    @Column(nullable = false)
    private Instant createdAt;

    protected AskRecordEntity() {
    }

    public AskRecordEntity(
            String id,
            String knowledgeBaseId,
            String question,
            String answer,
            String answerProvider,
            int sourceCount,
            Instant createdAt
    ) {
        this.id = id;
        this.knowledgeBaseId = knowledgeBaseId;
        this.question = question;
        this.answer = answer;
        this.answerProvider = answerProvider;
        this.sourceCount = sourceCount;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getKnowledgeBaseId() {
        return knowledgeBaseId;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getAnswerProvider() {
        return answerProvider;
    }

    public int getSourceCount() {
        return sourceCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
