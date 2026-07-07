import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getApiErrorMessage } from '@/api/http'
import {
  askKnowledgeBase,
  createKnowledgeBase,
  listKnowledgeDocuments,
  listKnowledgeBases,
  uploadKnowledgeDocument,
  type AskKnowledgeResponse,
  type KnowledgeDocumentSummary,
  type KnowledgeBaseSummary,
} from '@/api/knowledge'

export const useKnowledgeStore = defineStore('knowledge', () => {
  // 这些 ref 是页面共享状态：页面只负责展示，具体请求流程放在 store 里。
  const knowledgeBases = ref<KnowledgeBaseSummary[]>([])
  const currentKnowledgeBaseId = ref('demo')
  const loading = ref(false)
  const creating = ref(false)
  const uploading = ref(false)
  const errorMessage = ref<string | null>(null)
  const latestAnswer = ref<AskKnowledgeResponse | null>(null)
  const documents = ref<KnowledgeDocumentSummary[]>([])
  let processingPollTimer: number | null = null

  async function loadKnowledgeBases() {
    try {
      knowledgeBases.value = await listKnowledgeBases()
      currentKnowledgeBaseId.value = knowledgeBases.value[0]?.id ?? 'demo'
      // 知识库列表加载完后，顺手加载当前知识库的文档列表。
      await loadDocuments()
    } catch (error) {
      errorMessage.value = getApiErrorMessage(error)
    }
  }

  async function loadDocuments() {
    if (!currentKnowledgeBaseId.value) {
      documents.value = []
      return
    }

    try {
      documents.value = await listKnowledgeDocuments(currentKnowledgeBaseId.value)
      scheduleProcessingPollIfNeeded()
    } catch (error) {
      errorMessage.value = getApiErrorMessage(error)
    }
  }

  async function ask(question: string) {
    loading.value = true
    errorMessage.value = null
    try {
      latestAnswer.value = await askKnowledgeBase(currentKnowledgeBaseId.value, question)
    } catch (error) {
      errorMessage.value = getApiErrorMessage(error)
    } finally {
      loading.value = false
    }
  }

  async function create(name: string) {
    creating.value = true
    errorMessage.value = null
    try {
      const knowledgeBase = await createKnowledgeBase(name)
      await loadKnowledgeBases()
      currentKnowledgeBaseId.value = knowledgeBase.id
      documents.value = []
      latestAnswer.value = null
      return true
    } catch (error) {
      errorMessage.value = getApiErrorMessage(error)
      return false
    } finally {
      creating.value = false
    }
  }

  async function uploadDocument(file: File) {
    uploading.value = true
    errorMessage.value = null
    try {
      // 刷新知识库列表会重设 currentKnowledgeBaseId，所以这里先保存当前 ID。
      const knowledgeBaseId = currentKnowledgeBaseId.value
      await uploadKnowledgeDocument(currentKnowledgeBaseId.value, file)
      await loadKnowledgeBases()
      currentKnowledgeBaseId.value = knowledgeBaseId
      await loadDocuments()
      scheduleProcessingPollIfNeeded()
      return true
    } catch (error) {
      errorMessage.value = getApiErrorMessage(error)
      return false
    } finally {
      uploading.value = false
    }
  }

  function scheduleProcessingPollIfNeeded() {
    if (!documents.value.some((document) => document.processingStatus === 'PROCESSING')) {
      return
    }

    if (processingPollTimer !== null) {
      return
    }

    // 文档处理在后端异步进行，前端用轻量轮询刷新状态，直到没有 PROCESSING 文档。
    processingPollTimer = window.setTimeout(async () => {
      processingPollTimer = null
      await loadDocuments()
    }, 1500)
  }

  return {
    knowledgeBases,
    currentKnowledgeBaseId,
    loading,
    creating,
    uploading,
    errorMessage,
    latestAnswer,
    documents,
    loadKnowledgeBases,
    loadDocuments,
    ask,
    create,
    uploadDocument,
  }
})
