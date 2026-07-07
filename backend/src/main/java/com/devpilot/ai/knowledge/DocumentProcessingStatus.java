package com.devpilot.ai.knowledge;

public enum DocumentProcessingStatus {
    // 文档刚保存，还在解析、切分或准备后续 embedding。
    PROCESSING,

    // 文档已经处理完成，可以参与问答检索。
    READY,

    // 文档保存成功，但处理过程失败；后续可以做重试入口。
    FAILED
}
