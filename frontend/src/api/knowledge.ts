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

export interface KnowledgeDocumentSummary {
  id: string
  filename: string
  contentType: string | null
  sizeBytes: number
  createdAt: string
}

export async function listKnowledgeBases() {
  const { data } = await http.get<KnowledgeBaseSummary[]>('/knowledge-bases')
  return data
}

export async function createKnowledgeBase(name: string) {
  const { data } = await http.post<KnowledgeBaseSummary>('/knowledge-bases', { name })
  return data
}

export async function askKnowledgeBase(knowledgeBaseId: string, question: string) {
  const { data } = await http.post<AskKnowledgeResponse>(
    `/knowledge-bases/${knowledgeBaseId}/ask`,
    { question },
  )
  return data
}

export async function listKnowledgeDocuments(knowledgeBaseId: string) {
  const { data } = await http.get<KnowledgeDocumentSummary[]>(
    `/knowledge-bases/${knowledgeBaseId}/documents`,
  )
  return data
}

export async function uploadKnowledgeDocument(knowledgeBaseId: string, file: File) {
  const formData = new FormData()
  formData.append('file', file)

  const { data } = await http.post<KnowledgeDocumentSummary>(
    `/knowledge-bases/${knowledgeBaseId}/documents`,
    formData,
  )
  return data
}
