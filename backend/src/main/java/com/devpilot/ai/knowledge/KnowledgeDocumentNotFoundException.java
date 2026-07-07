package com.devpilot.ai.knowledge;

public class KnowledgeDocumentNotFoundException extends RuntimeException {

    public KnowledgeDocumentNotFoundException(String documentId) {
        super("Knowledge document not found: " + documentId);
    }
}
