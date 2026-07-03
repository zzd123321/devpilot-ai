package com.devpilot.ai.knowledge;

import jakarta.validation.constraints.NotBlank;

public record AskKnowledgeRequest(
        @NotBlank(message = "Question is required")
        String question
) {
}

