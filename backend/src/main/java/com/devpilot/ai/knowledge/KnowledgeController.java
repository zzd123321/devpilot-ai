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

    private final KnowledgeService knowledgeService;

    public KnowledgeController(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    @GetMapping
    public List<KnowledgeBaseSummary> listKnowledgeBases() {
        return knowledgeService.listKnowledgeBases();
    }

    @PostMapping("/{knowledgeBaseId}/ask")
    public AskKnowledgeResponse ask(
            @PathVariable String knowledgeBaseId,
            @Valid @RequestBody AskKnowledgeRequest request
    ) {
        return knowledgeService.ask(knowledgeBaseId, request.question());
    }
}
