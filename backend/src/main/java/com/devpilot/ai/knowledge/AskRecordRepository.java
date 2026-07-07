package com.devpilot.ai.knowledge;

import java.util.List;

public interface AskRecordRepository {

    List<AskRecord> findRecentByKnowledgeBaseId(String knowledgeBaseId);

    AskRecord save(AskRecord record);
}
