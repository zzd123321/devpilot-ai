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
            <article v-for="source in store.latestAnswer.sources" :key="source.documentName" class="source">
              <strong>{{ source.documentName }}</strong>
              <p>{{ source.snippet }}</p>
              <span>Score {{ source.score }}</span>
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
          <article v-for="document in store.documents" :key="document.id" class="document-item">
            <div>
              <strong>{{ document.filename }}</strong>
              <p>{{ document.chunkCount }} chunks</p>
            </div>
            <span>{{ formatBytes(document.sizeBytes) }}</span>
          </article>
        </div>
      </div>
    </section>
  </AppShell>
</template>
