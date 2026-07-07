package com.devpilot.ai.knowledge;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/knowledge-bases")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    // 构造函数注入：Controller 只依赖业务层，不直接依赖数据库或 JPA。
    public KnowledgeController(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    // GET /api/knowledge-bases：查询知识库列表，返回给前端下拉框使用。
    @GetMapping
    public List<KnowledgeBaseSummary> listKnowledgeBases() {
        return knowledgeService.listKnowledgeBases();
    }

    // POST /api/knowledge-bases：创建资源。成功时返回 201 Created 和 Location 响应头。
    @PostMapping
    public ResponseEntity<KnowledgeBaseSummary> createKnowledgeBase(
            @Valid @RequestBody CreateKnowledgeBaseRequest request
    ) {
        KnowledgeBaseSummary knowledgeBase = knowledgeService.createKnowledgeBase(request.name());
        URI location = URI.create("/api/knowledge-bases/" + knowledgeBase.id());
        return ResponseEntity.created(location).body(knowledgeBase);
    }

    // 当前是关键词检索版问答，后续会升级为：向量检索文档片段 -> 组装 Prompt -> 调用模型。
    @PostMapping("/{knowledgeBaseId}/ask")
    public AskKnowledgeResponse ask(
            @PathVariable String knowledgeBaseId,
            @Valid @RequestBody AskKnowledgeRequest request
    ) {
        return knowledgeService.ask(knowledgeBaseId, request.question());
    }

    // GET /api/knowledge-bases/{id}/documents：查询某个知识库下的文档列表。
    @GetMapping("/{knowledgeBaseId}/documents")
    public List<KnowledgeDocumentSummary> listDocuments(@PathVariable String knowledgeBaseId) {
        return knowledgeService.listDocuments(knowledgeBaseId);
    }

    // 文件上传不是 JSON 请求，而是 multipart/form-data；Spring 用 MultipartFile 接收文件。
    @PostMapping("/{knowledgeBaseId}/documents")
    public ResponseEntity<KnowledgeDocumentSummary> uploadDocument(
            @PathVariable String knowledgeBaseId,
            @RequestParam("file") MultipartFile file
    ) {
        KnowledgeDocumentSummary document = knowledgeService.uploadDocument(knowledgeBaseId, file);
        URI location = URI.create("/api/knowledge-bases/%s/documents/%s".formatted(knowledgeBaseId, document.id()));
        return ResponseEntity.created(location).body(document);
    }
}
