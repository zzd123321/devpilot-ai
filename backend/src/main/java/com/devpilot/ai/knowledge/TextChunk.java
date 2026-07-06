package com.devpilot.ai.knowledge;

public record TextChunk(
        int index,
        String content,
        int charStart,
        int charEnd
) {
}

