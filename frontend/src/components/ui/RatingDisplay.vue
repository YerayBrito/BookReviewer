<template>
  <div class="rating-display">
    <q-rating 
      v-model="ratingValue" 
      :size="size"
      color="amber"
      readonly
      :max="max"
    />
    <span v-if="showScore" class="q-ml-sm text-caption">
      {{ ratingValue.toFixed(1) }} / {{ max }}
    </span>
    <span v-if="showCount && reviewCount" class="q-ml-sm text-caption text-grey-6">
      ({{ reviewCount }} {{ reviewCount === 1 ? 'review' : 'reviews' }})
    </span>
  </div>
</template>

<script setup lang="ts">
interface Props {
  rating: number
  size?: string
  max?: number
  showScore?: boolean
  showCount?: boolean
  reviewCount?: number
}

const props = withDefaults(defineProps<Props>(), {
  size: '1.5em',
  max: 5,
  showScore: true,
  showCount: false,
  reviewCount: 0
})

const ratingValue = computed(() => props.rating)
</script>

<style scoped>
.rating-display {
  display: inline-flex;
  align-items: center;
}
</style> 