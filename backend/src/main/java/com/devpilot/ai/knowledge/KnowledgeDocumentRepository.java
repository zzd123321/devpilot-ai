package com.devpilot.ai.knowledge;

import java.util.List;

public interface KnowledgeDocumentRepository {

    List<KnowledgeDocument> findAllByKnowledgeBaseId(String knowledgeBaseId);

    KnowledgeDocument save(KnowledgeDocument document);
}

