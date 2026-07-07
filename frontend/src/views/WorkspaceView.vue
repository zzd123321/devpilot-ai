<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import AppShell from '@/components/AppShell.vue'
import { useKnowledgeStore } from '@/stores/knowledge'

const store = useKnowledgeStore()
const question = ref('请总结这个知识库里项目的核心模块。')
const newKnowledgeBaseName = ref('')
const selectedFile = ref<File | null>(null)

const selectedKnowledgeBase = computed(() =>
  store.knowledgeBases.find((item) => item.id === store.currentKnowledgeBaseId),
)
const selectedDocument = computed(() =>
  store.documents.find((document) => document.id === store.selectedDocumentId),
)

onMounted(() => {
  void store.loadKnowledgeBases()
})

watch(
  () => store.currentKnowledgeBaseId,
  () => {
    void store.loadDocuments()
  },
)

function submitQuestion() {
  const trimmed = question.value.trim()
  if (!trimmed) return
  void store.ask(trimmed)
}

function submitKnowledgeBase() {
  const trimmed = newKnowledgeBaseName.value.trim()
  if (!trimmed) return
  void store.create(trimmed).then((created) => {
    if (created) {
      newKnowledgeBaseName.value = ''
    }
  })
}

function selectDocument(event: Event) {
  const input = event.target as HTMLInputElement
  selectedFile.value = input.files?.[0] ?? null
}

function submitDocument() {
  if (!selectedFile.value) return
  void store.uploadDocument(selectedFile.value).then((uploaded) => {
    if (uploaded) {
      selectedFile.value = null
    }
  })
}

function formatBytes(sizeBytes: number) {
  if (sizeBytes < 1024) return `${sizeBytes} B`
  return `${(sizeBytes / 1024).toFixed(1)} KB`
}

function formatProcessingStatus(status: 'PROCESSING' | 'READY' | 'FAILED') {
  const statusText = {
    PROCESSING: 'Processing',
    READY: 'Ready',
    FAILED: 'Failed',
  }

  return statusText[status]
}
</script>

<template>
  <AppShell title="DevPilot AI">
    <section class="workspace-header">
      <div>
        <p class="eyebrow">AI Knowledge Base</p>
        <h1>Project assistant workspace</h1>
      </div>
      <select v-model="store.currentKnowledgeBaseId" class="select">
        <option v-for="item in store.knowledgeBases" :key="item.id" :value="item.id">
          {{ item.name }}
        </option>
      </select>
    </section>

    <section class="workspace-grid">
      <div class="panel">
        <div class="create-row">
          <input
            v-model="newKnowledgeBaseName"
            class="text-input"
            placeholder="New knowledge base"
            @keyup.enter="submitKnowledgeBase"
          />
          <button class="secondary-button" :disabled="store.creating" @click="submitKnowledgeBase">
            {{ store.creating ? 'Creating...' : 'Create' }}
          </button>
        </div>

        <p v-if="store.errorMessage" class="error-message">
          {{ store.errorMessage }}
        </p>

        <div class="panel-heading">
          <h2>Ask</h2>
          <span>{{ selectedKnowledgeBase?.documentCount ?? 0 }} docs</span>
        </div>
        <textarea v-model="question" class="question-input" rows="8" />
        <button class="primary-button" :disabled="store.loading" @click="submitQuestion">
          {{ store.loading ? 'Thinking...' : 'Ask knowledge base' }}
        </button>
      </div>

      <div class="panel answer-panel">
        <div class="panel-heading">
          <h2>Answer</h2>
        </div>
        <p v-if="!store.latestAnswer" class="empty-state">
          Your first AI answer will appear here.
        </p>
        <template v-else>
          <p class="answer">{{ store.latestAnswer.answer }}</p>
          <div class="sources">
            <h3>Sources</h3>
            <article v-for="source in store.latestAnswer.sources" :key="source.chunkId" class="source">
              <div class="source-heading">
                <strong>{{ source.documentName }}</strong>
                <span>Chunk #{{ source.chunkIndex + 1 }}</span>
              </div>
              <p>{{ source.snippet }}</p>
              <div class="source-meta">
                <span>Score {{ source.score }}</span>
                <span>{{ source.charStart }}-{{ source.charEnd }}</span>
              </div>
              <div v-if="source.matchedTerms.length" class="matched-terms">
                <span v-for="term in source.matchedTerms" :key="term">{{ term }}</span>
              </div>
            </article>
          </div>
        </template>
      </div>
    </section>

    <section class="documents-section">
      <div class="panel">
        <div class="panel-heading">
          <h2>Documents</h2>
          <span>{{ store.documents.length }} uploaded</span>
        </div>

        <div class="upload-row">
          <input class="file-input" type="file" accept=".txt,.md,.markdown" @change="selectDocument" />
          <button class="secondary-button" :disabled="!selectedFile || store.uploading" @click="submitDocument">
            {{ store.uploading ? 'Uploading...' : 'Upload' }}
          </button>
        </div>

        <p v-if="!store.documents.length" class="empty-state">
          Upload a Markdown or text file to start building this knowledge base.
        </p>
        <div v-else class="document-list">
          <button
            v-for="document in store.documents"
            :key="document.id"
            type="button"
            class="document-item document-item-button"
            :class="{ 'document-item-active': document.id === store.selectedDocumentId }"
            @click="store.selectDocument(document)"
          >
            <div>
              <div class="document-title-row">
                <strong>{{ document.filename }}</strong>
                <span class="status-badge" :class="`status-badge-${document.processingStatus.toLowerCase()}`">
                  {{ formatProcessingStatus(document.processingStatus) }}
                </span>
              </div>
              <p v-if="document.processingStatus === 'FAILED' && document.processingError">
                {{ document.processingError }}
              </p>
              <p v-else>{{ document.chunkCount }} chunks</p>
            </div>
            <span>{{ formatBytes(document.sizeBytes) }}</span>
          </button>
        </div>
      </div>

      <div class="panel chunk-panel">
        <div class="panel-heading">
          <h2>Chunks</h2>
          <span>{{ store.selectedDocumentChunks.length }} shown</span>
        </div>

        <p v-if="!selectedDocument" class="empty-state">
          Select a ready document to inspect its chunks.
        </p>
        <p v-else-if="selectedDocument.processingStatus === 'PROCESSING'" class="empty-state">
          This document is still processing.
        </p>
        <p v-else-if="selectedDocument.processingStatus === 'FAILED'" class="empty-state">
          This document failed to process.
        </p>
        <p v-else-if="store.loadingChunks" class="empty-state">
          Loading chunks...
        </p>
        <p v-else-if="!store.selectedDocumentChunks.length" class="empty-state">
          No chunks found for this document.
        </p>
        <div v-else class="chunk-list">
          <article v-for="chunk in store.selectedDocumentChunks" :key="chunk.id" class="chunk-item">
            <div class="chunk-meta">
              <strong>#{{ chunk.chunkIndex + 1 }}</strong>
              <span>{{ chunk.charStart }}-{{ chunk.charEnd }}</span>
            </div>
            <p>{{ chunk.content }}</p>
          </article>
        </div>
      </div>
    </section>
  </AppShell>
</template>
