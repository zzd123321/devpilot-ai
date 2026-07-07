package com.devpilot.ai.knowledge;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DocumentProcessingService {

    private final KnowledgeDocumentRepository documentRepository;
    private final DocumentChunkRepository chunkRepository;
    private final TextChunker textChunker;

    public DocumentProcessingService(
            KnowledgeDocumentRepository documentRepository,
            DocumentChunkRepository chunkRepository,
            TextChunker textChunker
    ) {
        this.documentRepository = documentRepository;
        this.chunkRepository = chunkRepository;
        this.textChunker = textChunker;
    }

    @Async("documentProcessingExecutor")
    @Transactional
    public void processDocument(String documentId) {
        // 后台线程只拿 documentId，再重新查数据库，避免跨线程传递 JPA 管理对象。
        documentRepository.findById(documentId).ifPresent(document -> {
            try {
                List<DocumentChunk> chunks = buildChunks(document);
                if (chunks.isEmpty()) {
                    throw new InvalidDocumentException("Document does not contain readable text.");
                }

                chunkRepository.saveAll(chunks);
                documentRepository.save(markDocumentReady(document));
            } catch (RuntimeException exception) {
                documentRepository.save(markDocumentFailed(document, exception));
            }
        });
    }

    private List<DocumentChunk> buildChunks(KnowledgeDocument document) {
        return textChunker.split(document.content())
                .stream()
                .map(chunk -> new DocumentChunk(
                        UUID.randomUUID().toString(),
                        document.knowledgeBaseId(),
                        document.id(),
                        chunk.index(),
                        chunk.content(),
                        chunk.charStart(),
                        chunk.charEnd(),
                        Instant.now()
                ))
                .toList();
    }

    private KnowledgeDocument markDocumentReady(KnowledgeDocument document) {
        return new KnowledgeDocument(
                document.id(),
                document.knowledgeBaseId(),
                document.filename(),
                document.contentType(),
                document.sizeBytes(),
                document.content(),
                DocumentProcessingStatus.READY,
                null,
                document.createdAt()
        );
    }

    private KnowledgeDocument markDocumentFailed(KnowledgeDocument document, RuntimeException exception) {
        return new KnowledgeDocument(
                document.id(),
                document.knowledgeBaseId(),
                document.filename(),
                document.contentType(),
                document.sizeBytes(),
                document.content(),
                DocumentProcessingStatus.FAILED,
                safeErrorMessage(exception),
                document.createdAt()
        );
    }

    private String safeErrorMessage(RuntimeException exception) {
        String message = exception.getMessage();
        if (message == null || message.isBlank()) {
            return "Document processing failed.";
        }
        return message.length() <= 500 ? message : message.substring(0, 500);
    }
}
