package com.devpilot.ai.knowledge;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/knowledge-bases")
public class KnowledgeController {

    @GetMapping
    public List<KnowledgeBaseSummary> listKnowledgeBases() {
        return List.of(
                new KnowledgeBaseSummary("demo", "Demo Knowledge Base", 0)
        );
    }

    @PostMapping("/{knowledgeBaseId}/ask")
    public AskKnowledgeResponse ask(
            @PathVariable String knowledgeBaseId,
            @Valid @RequestBody AskKnowledgeRequest request
    ) {
        return new AskKnowledgeResponse(
                "This is a placeholder answer for knowledge base `%s`: %s"
                        .formatted(knowledgeBaseId, request.question()),
                List.of(new SourceReference("README.md", "Project overview", 0.92))
        );
    }
}

