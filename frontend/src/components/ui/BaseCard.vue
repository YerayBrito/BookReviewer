<template>
  <q-card 
    :class="cardClass" 
    :elevation="elevation"
    :flat="flat"
    :bordered="bordered"
  >
    <q-card-section v-if="$slots.header || title" :class="headerClass">
      <slot name="header">
        <div class="text-h6">{{ title }}</div>
      </slot>
    </q-card-section>

    <q-card-section :class="contentClass">
      <slot />
    </q-card-section>

    <q-card-actions v-if="$slots.actions" :class="actionsClass">
      <slot name="actions" />
    </q-card-actions>
  </q-card>
</template>

<script setup lang="ts">
interface Props {
  title?: string
  elevation?: number
  flat?: boolean
  bordered?: boolean
  cardClass?: string
  headerClass?: string
  contentClass?: string
  actionsClass?: string
}

withDefaults(defineProps<Props>(), {
  elevation: 1,
  flat: false,
  bordered: false,
  cardClass: '',
  headerClass: '',
  contentClass: '',
  actionsClass: ''
})
</script> 