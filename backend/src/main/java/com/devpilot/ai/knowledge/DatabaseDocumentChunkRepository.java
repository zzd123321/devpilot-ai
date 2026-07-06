package com.devpilot.ai.knowledge;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseDocumentChunkRepository implements DocumentChunkRepository {

    private final JpaDocumentChunkRepository jpaRepository;

    public DatabaseDocumentChunkRepository(JpaDocumentChunkRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<DocumentChunk> findAllByDocumentId(String documentId) {
        return jpaRepository.findAllByDocumentIdOrderByChunkIndexAsc(documentId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<DocumentChunk> findAllByKnowledgeBaseId(String knowledgeBaseId) {
        return jpaRepository.findAllByKnowledgeBaseIdOrderByCreatedAtDesc(knowledgeBaseId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public long countByDocumentId(String documentId) {
        return jpaRepository.countByDocumentId(documentId);
    }

    @Override
    public List<DocumentChunk> saveAll(List<DocumentChunk> chunks) {
        List<DocumentChunkEntity> entities = chunks.stream()
                .map(this::toEntity)
                .toList();
        return jpaRepository.saveAll(entities)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private DocumentChunkEntity toEntity(DocumentChunk chunk) {
        return new DocumentChunkEntity(
                chunk.id(),
                chunk.knowledgeBaseId(),
                chunk.documentId(),
                chunk.chunkIndex(),
                chunk.content(),
                chunk.charStart(),
                chunk.charEnd(),
                chunk.createdAt()
        );
    }

    private DocumentChunk toDomain(DocumentChunkEntity entity) {
        return new DocumentChunk(
                entity.getId(),
                entity.getKnowledgeBaseId(),
                entity.getDocumentId(),
                entity.getChunkIndex(),
                entity.getContent(),
                entity.getCharStart(),
                entity.getCharEnd(),
                entity.getCreatedAt()
        );
    }
}
