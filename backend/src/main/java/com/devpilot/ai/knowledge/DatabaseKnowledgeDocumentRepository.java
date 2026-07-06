package com.devpilot.ai.knowledge;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseKnowledgeDocumentRepository implements KnowledgeDocumentRepository {

    private final JpaKnowledgeDocumentRepository jpaRepository;

    // 文档数据访问适配器：Service 不需要知道底层是 JpaRepository。
    public DatabaseKnowledgeDocumentRepository(JpaKnowledgeDocumentRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<KnowledgeDocument> findAllByKnowledgeBaseId(String knowledgeBaseId) {
        // 数据库层按上传时间倒序返回，前端能优先看到最新上传的文档。
        return jpaRepository.findAllByKnowledgeBaseIdOrderByCreatedAtDesc(knowledgeBaseId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<KnowledgeDocument> findById(String id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public KnowledgeDocument save(KnowledgeDocument document) {
        KnowledgeDocumentEntity entity = new KnowledgeDocumentEntity(
                document.id(),
                document.knowledgeBaseId(),
                document.filename(),
                document.contentType(),
                document.sizeBytes(),
                document.content(),
                document.createdAt()
        );
        return toDomain(jpaRepository.save(entity));
    }

    private KnowledgeDocument toDomain(KnowledgeDocumentEntity entity) {
        return new KnowledgeDocument(
                entity.getId(),
                entity.getKnowledgeBaseId(),
                entity.getFilename(),
                entity.getContentType(),
                entity.getSizeBytes(),
                entity.getContent(),
                entity.getCreatedAt()
        );
    }
}
