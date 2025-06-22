<template>
  <div class="loading-container" :class="containerClass">
    <q-spinner-dots :size="size" :color="color" />
    <div v-if="message" class="q-mt-sm text-center">{{ message }}</div>
  </div>
</template>

<script setup lang="ts">
interface Props {
  size?: string
  color?: string
  message?: string
  containerClass?: string
}

withDefaults(defineProps<Props>(), {
  size: '50px',
  color: 'primary',
  message: '',
  containerClass: ''
})
</script>

<style scoped>
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 2rem;
}
</style> 