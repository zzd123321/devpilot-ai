<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import AppShell from '@/components/AppShell.vue'
import { useKnowledgeStore } from '@/stores/knowledge'

const store = useKnowledgeStore()
const question = ref('请总结这个知识库里项目的核心模块。')

const selectedKnowledgeBase = computed(() =>
  store.knowledgeBases.find((item) => item.id === store.currentKnowledgeBaseId),
)

onMounted(() => {
  void store.loadKnowledgeBases()
})

function submitQuestion() {
  const trimmed = question.value.trim()
  if (!trimmed) return
  void store.ask(trimmed)
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
  </AppShell>
</template>

