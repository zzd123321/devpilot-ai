package com.devpilot.ai.knowledge;

public class KnowledgeBaseNotFoundException extends RuntimeException {

    public KnowledgeBaseNotFoundException(String knowledgeBaseId) {
        super("Knowledge base not found: " + knowledgeBaseId);
    }
}
