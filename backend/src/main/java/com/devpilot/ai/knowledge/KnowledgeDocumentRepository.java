package com.devpilot.ai.knowledge;

import java.util.List;
import java.util.Optional;

public interface KnowledgeDocumentRepository {

    List<KnowledgeDocument> findAllByKnowledgeBaseId(String knowledgeBaseId);

    Optional<KnowledgeDocument> findById(String id);

    KnowledgeDocument save(KnowledgeDocument document);
}
