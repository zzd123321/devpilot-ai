package com.devpilot.ai.chat;

import com.devpilot.ai.knowledge.SourceReference;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LocalMockChatAnswerClient implements ChatAnswerClient {

    private final String provider;

    public LocalMockChatAnswerClient(@Value("${devpilot.ai.provider:mock}") String provider) {
        this.provider = provider;
    }

    @Override
    public ChatAnswer generate(ChatAnswerRequest request) {
        if (!"mock".equalsIgnoreCase(provider)) {
            return new ChatAnswer(
                    provider,
                    "当前配置的 AI provider 是 `%s`，但真实模型调用还没有接入。本地先保留 prompt 和 sources，下一步会接入真正的模型客户端。"
                            .formatted(provider)
            );
        }

        String sourceSummary = request.sources()
                .stream()
                .limit(3)
                .map(this::summarizeSource)
                .collect(Collectors.joining("\n"));

        return new ChatAnswer(
                "mock",
                """
                这是本地 Mock AI 生成的练习回答，还没有调用真实大模型。

                我根据当前检索到的资料，先给你一个结构化草稿：
                %s

                你可以重点检查这些 sources 是否真的回答了问题。如果 sources 不相关，后续即使接入真实模型，回答质量也会受影响。
                """.formatted(sourceSummary)
        );
    }

    private String summarizeSource(SourceReference source) {
        return "- `%s` 的 Chunk #%d 命中关键词 %s，片段摘要：%s"
                .formatted(
                        source.documentName(),
                        source.chunkIndex() + 1,
                        source.matchedTerms(),
                        source.snippet()
                );
    }
}
