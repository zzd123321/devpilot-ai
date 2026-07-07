package com.devpilot.ai.knowledge;

import java.time.Instant;

public record KnowledgeDocument(
        String id,
        String knowledgeBaseId,
        String filename,
        String contentType,
        long sizeBytes,
        String content,
        DocumentProcessingStatus processingStatus,
        String processingError,
        Instant createdAt
) {
}
