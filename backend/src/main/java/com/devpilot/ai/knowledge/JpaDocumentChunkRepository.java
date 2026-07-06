package com.devpilot.ai.knowledge;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDocumentChunkRepository extends JpaRepository<DocumentChunkEntity, String> {

    List<DocumentChunkEntity> findAllByDocumentIdOrderByChunkIndexAsc(String documentId);

    List<DocumentChunkEntity> findAllByKnowledgeBaseIdOrderByCreatedAtDesc(String knowledgeBaseId);

    long countByDocumentId(String documentId);
}
