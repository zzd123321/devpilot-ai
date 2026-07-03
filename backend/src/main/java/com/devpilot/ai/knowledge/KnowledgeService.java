package com.devpilot.ai.knowledge;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class KnowledgeService {

    private final KnowledgeRepository knowledgeRepository;

    public KnowledgeService(KnowledgeRepository knowledgeRepository) {
        this.knowledgeRepository = knowledgeRepository;
    }

    public List<KnowledgeBaseSummary> listKnowledgeBases() {
        return knowledgeRepository.findAll()
                .stream()
                .map(this::toSummary)
                .toList();
    }

    public AskKnowledgeResponse ask(String knowledgeBaseId, String question) {
        KnowledgeBase knowledgeBase = knowledgeRepository.findById(knowledgeBaseId)
                .orElseThrow(() -> new KnowledgeBaseNotFoundException(knowledgeBaseId));

        return new AskKnowledgeResponse(
                "This is a placeholder answer for knowledge base `%s`: %s"
                        .formatted(knowledgeBase.id(), question),
                List.of(new SourceReference("README.md", "Project overview", 0.92))
        );
    }

    private KnowledgeBaseSummary toSummary(KnowledgeBase knowledgeBase) {
        return new KnowledgeBaseSummary(
                knowledgeBase.id(),
                knowledgeBase.name(),
                knowledgeBase.documentCount()
        );
    }
}
