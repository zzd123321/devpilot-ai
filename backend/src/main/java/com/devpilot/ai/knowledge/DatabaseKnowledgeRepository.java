package com.devpilot.ai.knowledge;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseKnowledgeRepository implements KnowledgeRepository {

    private final JpaKnowledgeBaseRepository jpaRepository;

    // 这个类是“适配器”：把 Spring Data JPA 的能力包装成我们自己的 KnowledgeRepository。
    public DatabaseKnowledgeRepository(JpaKnowledgeBaseRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<KnowledgeBase> findAll() {
        return jpaRepository.findAllByOrderByNameAsc()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<KnowledgeBase> findById(String id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public KnowledgeBase save(KnowledgeBase knowledgeBase) {
        KnowledgeBaseEntity entity = new KnowledgeBaseEntity(
                knowledgeBase.id(),
                knowledgeBase.name(),
                knowledgeBase.documentCount(),
                Instant.now()
        );
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public void incrementDocumentCount(String id) {
        // 这个方法依赖外层 Service 的事务。实体被查出来后处于托管状态，修改字段会自动同步到数据库。
        jpaRepository.findById(id).ifPresent(KnowledgeBaseEntity::incrementDocumentCount);
    }

    private KnowledgeBase toDomain(KnowledgeBaseEntity entity) {
        return new KnowledgeBase(
                entity.getId(),
                entity.getName(),
                entity.getDocumentCount()
        );
    }
}
