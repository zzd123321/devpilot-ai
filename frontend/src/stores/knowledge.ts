import { defineStore } from 'pinia'
import { ref } from 'vue'
import {
  askKnowledgeBase,
  listKnowledgeBases,
  type AskKnowledgeResponse,
  type KnowledgeBaseSummary,
} from '@/api/knowledge'

export const useKnowledgeStore = defineStore('knowledge', () => {
  const knowledgeBases = ref<KnowledgeBaseSummary[]>([])
  const currentKnowledgeBaseId = ref('demo')
  const loading = ref(false)
  const latestAnswer = ref<AskKnowledgeResponse | null>(null)

  async function loadKnowledgeBases() {
    knowledgeBases.value = await listKnowledgeBases()
    currentKnowledgeBaseId.value = knowledgeBases.value[0]?.id ?? 'demo'
  }

  async function ask(question: string) {
    loading.value = true
    try {
      latestAnswer.value = await askKnowledgeBase(currentKnowledgeBaseId.value, question)
    } finally {
      loading.value = false
    }
  }

  return {
    knowledgeBases,
    currentKnowledgeBaseId,
    loading,
    latestAnswer,
    loadKnowledgeBases,
    ask,
  }
})

