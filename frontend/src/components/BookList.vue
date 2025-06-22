<template>
  <div>
    <q-page-header class="text-h4 q-mb-md">
      Book Catalog
    </q-page-header>

    <!-- Loading State -->
    <LoadingSpinner 
      v-if="loading" 
      message="Loading books..."
    />

    <!-- Error State -->
    <q-banner v-if="error" class="text-white bg-negative q-mb-md">
      {{ error }}
    </q-banner>

    <!-- Books Grid -->
    <div v-if="!loading && !error" data-cy="book-list">
      <q-list separator>
        <q-item 
          v-for="book in books" 
          :key="book.id" 
          data-cy="book-item"
          clickable 
          v-ripple
          @click="selectBook(book)"
          class="q-mb-md"
        >
          <q-item-section>
            <BaseCard>
              <q-card-section>
                <div class="row items-center justify-between">
                  <div class="col">
                    <div class="text-h6" data-cy="book-title">{{ book.title }}</div>
                    <div class="text-subtitle2 text-grey-7" data-cy="book-author">
                      Author: {{ book.author }}
                    </div>
                    
                    <div class="row items-center q-mt-sm">
                      <RatingDisplay 
                        v-if="book.averageRating"
                        :rating="book.averageRating"
                        :review-count="book.reviewsCount"
                        :show-count="true"
                        size="1.5em"
                      />
                    </div>
                  </div>
                  
                  <div class="col-auto">
                    <q-icon name="chevron_right" size="sm" color="grey-5" />
                  </div>
                </div>
              </q-card-section>
            </BaseCard>
          </q-item-section>
        </q-item>
      </q-list>

      <!-- Empty State -->
      <EmptyState 
        v-if="books.length === 0"
        icon="library_books"
        title="No books available"
        description="Check back later for new titles"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { apiService } from '../services/api'
import BaseCard from './ui/BaseCard.vue'
import RatingDisplay from './ui/RatingDisplay.vue'
import LoadingSpinner from './ui/LoadingSpinner.vue'
import EmptyState from './ui/EmptyState.vue'

interface Book {
  id: number
  title: string
  author: string
  averageRating?: number
  reviewsCount?: number
}

const books = ref<Book[]>([])
const loading = ref(true)
const error = ref('')

const emit = defineEmits<{
  bookSelected: [book: Book]
}>()

const selectBook = (book: Book) => {
  emit('bookSelected', book)
}

const fetchBooks = async () => {
  try {
    loading.value = true
    error.value = ''
    
    const response = await apiService.getBooks()
    
    if (response.error) {
      error.value = response.error
    } else if (response.data) {
      books.value = response.data
    }
  } catch (err) {
    error.value = `Network error: ${err instanceof Error ? err.message : 'Is the backend running?'}`
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchBooks()
})
</script> 