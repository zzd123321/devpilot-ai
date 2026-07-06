package com.devpilot.ai.knowledge;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateKnowledgeBaseRequest(
        @NotBlank(message = "Knowledge base name is required")
        @Size(max = 80, message = "Knowledge base name must be 80 characters or fewer")
        String name
) {
}
