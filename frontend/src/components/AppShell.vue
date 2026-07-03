<script setup lang="ts">
defineProps<{
  title: string
}>()
</script>

<template>
  <div class="app-shell">
    <aside class="sidebar">
      <div class="brand">
        <span class="brand-mark">D</span>
        <span>{{ title }}</span>
      </div>
      <nav class="nav">
        <a class="nav-item active" href="/">Knowledge</a>
        <a class="nav-item" href="/">Chats</a>
        <a class="nav-item" href="/">Prompts</a>
      </nav>
    </aside>
    <main class="content">
      <slot />
    </main>
  </div>
</template>

