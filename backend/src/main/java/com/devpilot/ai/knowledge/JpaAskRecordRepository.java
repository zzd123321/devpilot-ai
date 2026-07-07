package com.devpilot.ai.knowledge;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAskRecordRepository extends JpaRepository<AskRecordEntity, String> {

    List<AskRecordEntity> findTop20ByKnowledgeBaseIdOrderByCreatedAtDesc(String knowledgeBaseId);
}
