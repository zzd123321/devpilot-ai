package com.devpilot.ai.chat;

import com.devpilot.ai.knowledge.SourceReference;
import java.util.List;

public record ChatAnswerRequest(
        String question,
        String prompt,
        List<SourceReference> sources
) {
}
