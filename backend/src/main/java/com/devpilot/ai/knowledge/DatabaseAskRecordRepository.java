package com.devpilot.ai.knowledge;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseAskRecordRepository implements AskRecordRepository {

    private final JpaAskRecordRepository jpaRepository;

    public DatabaseAskRecordRepository(JpaAskRecordRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<AskRecord> findRecentByKnowledgeBaseId(String knowledgeBaseId) {
        return jpaRepository.findTop20ByKnowledgeBaseIdOrderByCreatedAtDesc(knowledgeBaseId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public AskRecord save(AskRecord record) {
        AskRecordEntity entity = new AskRecordEntity(
                record.id(),
                record.knowledgeBaseId(),
                record.question(),
                record.answer(),
                record.answerProvider(),
                record.sourceCount(),
                record.createdAt()
        );
        return toDomain(jpaRepository.save(entity));
    }

    private AskRecord toDomain(AskRecordEntity entity) {
        return new AskRecord(
                entity.getId(),
                entity.getKnowledgeBaseId(),
                entity.getQuestion(),
                entity.getAnswer(),
                entity.getAnswerProvider(),
                entity.getSourceCount(),
                entity.getCreatedAt()
        );
    }
}
