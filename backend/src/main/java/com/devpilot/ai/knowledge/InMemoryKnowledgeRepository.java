package com.devpilot.ai.knowledge;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryKnowledgeRepository implements KnowledgeRepository {

    private final Map<String, KnowledgeBase> knowledgeBases = Map.of(
            "demo", new KnowledgeBase("demo", "Demo Knowledge Base", 0)
    );

    @Override
    public List<KnowledgeBase> findAll() {
        return List.copyOf(knowledgeBases.values());
    }

    @Override
    public Optional<KnowledgeBase> findById(String id) {
        return Optional.ofNullable(knowledgeBases.get(id));
    }
}

