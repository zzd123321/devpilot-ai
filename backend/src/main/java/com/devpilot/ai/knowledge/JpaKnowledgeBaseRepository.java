package com.devpilot.ai.knowledge;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaKnowledgeBaseRepository extends JpaRepository<KnowledgeBaseEntity, String> {

    List<KnowledgeBaseEntity> findAllByOrderByNameAsc();
}

