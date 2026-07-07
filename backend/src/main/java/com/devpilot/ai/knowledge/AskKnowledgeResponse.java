package com.devpilot.ai.knowledge;

import java.util.List;

public record AskKnowledgeResponse(
        String answer,
        String promptPreview,
        List<SourceReference> sources
) {
}
