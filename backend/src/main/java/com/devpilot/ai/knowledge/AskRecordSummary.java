package com.devpilot.ai.knowledge;

import java.time.Instant;

public record AskRecordSummary(
        String id,
        String question,
        String answer,
        String answerProvider,
        String promptPreview,
        String sourcesJson,
        int sourceCount,
        Instant createdAt
) {
}
