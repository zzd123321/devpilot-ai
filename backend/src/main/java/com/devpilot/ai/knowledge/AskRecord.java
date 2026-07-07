package com.devpilot.ai.knowledge;

import java.time.Instant;

public record AskRecord(
        String id,
        String knowledgeBaseId,
        String question,
        String answer,
        String answerProvider,
        int sourceCount,
        Instant createdAt
) {
}
