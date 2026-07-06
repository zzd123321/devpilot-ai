package com.devpilot.ai.common;

import com.devpilot.ai.knowledge.KnowledgeBaseNotFoundException;
import com.devpilot.ai.knowledge.InvalidDocumentException;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 业务资源不存在时返回 404。这样前端能明确知道不是服务器崩了，而是请求的资源不存在。
    @ExceptionHandler(KnowledgeBaseNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleKnowledgeBaseNotFound(KnowledgeBaseNotFoundException exception) {
        ApiErrorResponse response = new ApiErrorResponse(
                "KNOWLEDGE_BASE_NOT_FOUND",
                exception.getMessage(),
                Instant.now(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // @Valid 校验失败会进入这里，把字段级错误整理成统一结构返回给前端。
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationError(MethodArgumentNotValidException exception) {
        List<FieldErrorResponse> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new FieldErrorResponse(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                ))
                .toList();

        ApiErrorResponse response = new ApiErrorResponse(
                "VALIDATION_ERROR",
                "Request validation failed",
                Instant.now(),
                fieldErrors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 文档上传的业务校验错误，比如空文件、文件过大、类型不支持。
    @ExceptionHandler(InvalidDocumentException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidDocument(InvalidDocumentException exception) {
        ApiErrorResponse response = new ApiErrorResponse(
                "INVALID_DOCUMENT",
                exception.getMessage(),
                Instant.now(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
