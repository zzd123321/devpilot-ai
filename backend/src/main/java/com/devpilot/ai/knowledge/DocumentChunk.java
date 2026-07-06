package com.devpilot.ai.knowledge;

import java.time.Instant;

public record DocumentChunk(
        String id,
        String knowledgeBaseId,
        String documentId,
        int chunkIndex,
        String content,
        int charStart,
        int charEnd,
        Instant createdAt
) {
}

