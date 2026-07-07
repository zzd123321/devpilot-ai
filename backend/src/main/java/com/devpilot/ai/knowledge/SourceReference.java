package com.devpilot.ai.knowledge;

import java.util.List;

public record SourceReference(
        String documentId,
        String documentName,
        String chunkId,
        int chunkIndex,
        int charStart,
        int charEnd,
        String snippet,
        double score,
        List<String> matchedTerms
) {
}
