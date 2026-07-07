package com.devpilot.ai.knowledge;

import java.time.Instant;

public record KnowledgeDocumentSummary(
        String id,
        String filename,
        String contentType,
        long sizeBytes,
        DocumentProcessingStatus processingStatus,
        String processingError,
        Instant createdAt,
        long chunkCount
) {
}
