package com.devpilot.ai.knowledge;

public record DocumentChunkSummary(
        String id,
        int chunkIndex,
        String content,
        int charStart,
        int charEnd
) {
}
