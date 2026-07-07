import { http } from './http'

export interface KnowledgeBaseSummary {
  id: string
  name: string
  documentCount: number
}

export interface AskKnowledgeResponse {
  answer: string
  answerProvider: string
  promptPreview: string
  sources: Array<{
    documentId: string
    documentName: string
    chunkId: string
    chunkIndex: number
    charStart: number
    charEnd: number
    snippet: string
    score: number
    matchedTerms: string[]
  }>
}

export interface KnowledgeDocumentSummary {
  id: string
  filename: string
  contentType: string | null
  sizeBytes: number
  processingStatus: 'PROCESSING' | 'READY' | 'FAILED'
  processingError: string | null
  createdAt: string
  chunkCount: number
}

export interface DocumentChunkSummary {
  id: string
  chunkIndex: number
  content: string
  charStart: number
  charEnd: number
}

export interface AskRecordSummary {
  id: string
  question: string
  answer: string
  answerProvider: string
  sourceCount: number
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

export async function listAskRecords(knowledgeBaseId: string) {
  const { data } = await http.get<AskRecordSummary[]>(
    `/knowledge-bases/${knowledgeBaseId}/ask-records`,
  )
  return data
}

export async function listKnowledgeDocuments(knowledgeBaseId: string) {
  const { data } = await http.get<KnowledgeDocumentSummary[]>(
    `/knowledge-bases/${knowledgeBaseId}/documents`,
  )
  return data
}

export async function listDocumentChunks(knowledgeBaseId: string, documentId: string) {
  const { data } = await http.get<DocumentChunkSummary[]>(
    `/knowledge-bases/${knowledgeBaseId}/documents/${documentId}/chunks`,
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
