package com.devpilot.ai.knowledge;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaKnowledgeDocumentRepository extends JpaRepository<KnowledgeDocumentEntity, String> {

    List<KnowledgeDocumentEntity> findAllByKnowledgeBaseIdOrderByCreatedAtDesc(String knowledgeBaseId);
}

