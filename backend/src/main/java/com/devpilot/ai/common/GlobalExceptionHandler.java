package com.devpilot.ai.common;

import com.devpilot.ai.knowledge.KnowledgeBaseNotFoundException;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(KnowledgeBaseNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleKnowledgeBaseNotFound(KnowledgeBaseNotFoundException exception) {
        ApiErrorResponse response = new ApiErrorResponse(
                "KNOWLEDGE_BASE_NOT_FOUND",
                exception.getMessage(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
