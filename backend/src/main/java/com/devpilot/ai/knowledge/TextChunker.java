package com.devpilot.ai.knowledge;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TextChunker {

    private static final int CHUNK_SIZE = 800;
    private static final int CHUNK_OVERLAP = 120;

    public List<TextChunk> split(String text) {
        String normalizedText = normalize(text);
        if (normalizedText.isBlank()) {
            return List.of();
        }

        List<TextChunk> chunks = new ArrayList<>();
        int start = 0;
        int index = 0;

        while (start < normalizedText.length()) {
            int end = Math.min(start + CHUNK_SIZE, normalizedText.length());
            String content = normalizedText.substring(start, end).trim();

            if (!content.isBlank()) {
                chunks.add(new TextChunk(index, content, start, end));
                index++;
            }

            if (end == normalizedText.length()) {
                break;
            }

            // 保留一段重叠文本，避免答案所需信息刚好被切在两个 chunk 边界上。
            start = Math.max(end - CHUNK_OVERLAP, start + 1);
        }

        return chunks;
    }

    private String normalize(String text) {
        return text.replace("\r\n", "\n").replace("\r", "\n").trim();
    }
}

