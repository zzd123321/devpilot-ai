package com.devpilot.ai.knowledge;

import java.util.List;

public interface DocumentChunkRepository {

    List<DocumentChunk> findAllByDocumentId(String documentId);

    List<DocumentChunk> findAllByKnowledgeBaseId(String knowledgeBaseId);

    long countByDocumentId(String documentId);

    List<DocumentChunk> saveAll(List<DocumentChunk> chunks);
}
