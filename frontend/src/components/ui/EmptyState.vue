<template>
  <q-banner class="text-center q-pa-lg empty-state">
    <q-icon :name="icon" :size="iconSize" :color="iconColor" class="q-mb-md" />
    <div class="text-h6 text-grey-7">{{ title }}</div>
    <div class="text-caption text-grey-6 q-mb-md">{{ description }}</div>
    <slot name="action" />
  </q-banner>
</template>

<script setup lang="ts">
interface Props {
  icon?: string
  iconSize?: string
  iconColor?: string
  title: string
  description: string
}

withDefaults(defineProps<Props>(), {
  icon: 'info',
  iconSize: '3em',
  iconColor: 'grey-5'
})
</script>

<style scoped>
.empty-state {
  border-radius: 8px;
}
</style> 