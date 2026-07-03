package com.devpilot.ai.knowledge;

public record SourceReference(
        String documentName,
        String snippet,
        double score
) {
}

