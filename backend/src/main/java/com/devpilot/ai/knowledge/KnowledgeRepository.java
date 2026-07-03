package com.devpilot.ai.knowledge;

import java.util.List;
import java.util.Optional;

public interface KnowledgeRepository {

    List<KnowledgeBase> findAll();

    Optional<KnowledgeBase> findById(String id);
}

