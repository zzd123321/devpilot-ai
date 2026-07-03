import { http } from './http'

export interface KnowledgeBaseSummary {
  id: string
  name: string
  documentCount: number
}

export interface AskKnowledgeResponse {
  answer: string
  sources: Array<{
    documentName: string
    snippet: string
    score: number
  }>
}

export async function listKnowledgeBases() {
  const { data } = await http.get<KnowledgeBaseSummary[]>('/knowledge-bases')
  return data
}

export async function askKnowledgeBase(knowledgeBaseId: string, question: string) {
  const { data } = await http.post<AskKnowledgeResponse>(
    `/knowledge-bases/${knowledgeBaseId}/ask`,
    { question },
  )
  return data
}

