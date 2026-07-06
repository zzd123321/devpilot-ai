package com.devpilot.ai.knowledge;

import java.time.Instant;

public record KnowledgeDocumentSummary(
        String id,
        String filename,
        String contentType,
        long sizeBytes,
        Instant createdAt,
        long chunkCount
) {
}
