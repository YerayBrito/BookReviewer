<template>
  <div v-if="book" data-cy="book-detail">
    <!-- Book Header -->
    <BaseCard class="q-mb-lg">
      <template #header>
        <div class="text-center">
          <div class="text-h4 q-mb-sm" data-cy="book-title">{{ book.title }}</div>
          <div class="text-h6 text-grey-7 q-mb-md" data-cy="book-author">
            Author: {{ book.author }}
          </div>
          
          <RatingDisplay 
            v-if="averageRating"
            :rating="averageRating"
            :review-count="book.reviews?.length || 0"
            :show-count="true"
            size="2em"
          />
        </div>
      </template>
    </BaseCard>

    <!-- Reviews Section -->
    <BaseCard data-cy="reviews-section" title="Reader Reviews">
      <div v-if="book.reviews && book.reviews.length > 0">
        <q-list separator>
          <q-item v-for="review in book.reviews" :key="review.id">
            <q-item-section>
              <q-item-label class="row items-center justify-between">
                <span class="text-weight-medium">
                  {{ review.user?.username || 'Anonymous' }}
                </span>
                <RatingDisplay 
                  :rating="review.rating"
                  :show-score="false"
                  size="1em"
                />
              </q-item-label>
              <q-item-label caption v-if="review.comment">
                {{ review.comment }}
              </q-item-label>
            </q-item-section>
          </q-item>
        </q-list>
      </div>
      
      <EmptyState 
        v-else
        icon="rate_review"
        title="No reviews yet"
        description="Be the first to review this book!"
      />
    </BaseCard>

    <!-- Action Buttons -->
    <div class="row justify-center q-gutter-md q-mt-lg">
      <q-btn 
        color="secondary" 
        icon="arrow_back" 
        label="Back to Catalog"
        @click="$emit('back')"
      />
      <q-btn 
        color="primary" 
        icon="rate_review" 
        label="Write Review"
        @click="showReviewForm = true"
      />
    </div>

    <!-- Review Form Dialog -->
    <q-dialog v-model="showReviewForm">
      <q-card style="min-width: 350px" data-cy="review-form">
        <q-card-section>
          <div class="text-h6">Write Your Review</div>
        </q-card-section>

        <q-card-section>
          <q-form @submit="submitReview" class="q-gutter-md">
            <div>
              <label class="text-weight-medium">Rating:</label>
              <q-rating 
                v-model="newReview.rating" 
                size="2em"
                color="amber"
                class="q-mt-sm"
                data-cy="rating-input"
              />
            </div>
            
            <q-input
              v-model="newReview.comment"
              type="textarea"
              label="Comment (optional)"
              rows="4"
              maxlength="500"
              counter
              placeholder="Share your thoughts about this book..."
              data-cy="comment-input"
            />
            
            <div class="row justify-end q-gutter-sm">
              <q-btn 
                flat 
                label="Cancel" 
                color="secondary"
                @click="showReviewForm = false"
              />
              <q-btn 
                unelevated 
                label="Submit Review" 
                type="submit"
                color="primary"
                :loading="submitting"
              />
            </div>
          </q-form>
        </q-card-section>
      </q-card>
    </q-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { apiService } from '../services/api'
import BaseCard from './ui/BaseCard.vue'
import RatingDisplay from './ui/RatingDisplay.vue'
import EmptyState from './ui/EmptyState.vue'

interface User {
  id: number
  username: string
  email: string
}

interface Review {
  id: number
  rating: number
  comment?: string
  user?: User
}

interface Book {
  id: number
  title: string
  author: string
  reviews?: Review[]
}

const props = defineProps<{
  book: Book | null
}>()

const emit = defineEmits<{
  back: []
  reviewSubmitted: []
}>()

const showReviewForm = ref(false)
const newReview = ref({
  rating: 0,
  comment: ''
})
const submitting = ref(false)

const averageRating = computed(() => {
  if (!props.book?.reviews || props.book.reviews.length === 0) return null
  const sum = props.book.reviews.reduce((acc, review) => acc + review.rating, 0)
  return sum / props.book.reviews.length
})

const submitReview = async () => {
  if (!props.book || !newReview.value.rating) return

  submitting.value = true

  try {
    const response = await apiService.createReview({
      bookId: props.book.id,
      userId: 1, // Hardcoded for demo
      rating: newReview.value.rating,
      comment: newReview.value.comment.trim()
    })

    if (response.error) {
      alert(`Failed to submit review: ${response.error}`)
    } else {
      showReviewForm.value = false
      newReview.value = { rating: 0, comment: '' }
      emit('reviewSubmitted')
    }
  } catch (error) {
    alert('Network error: Backend might not be running')
  } finally {
    submitting.value = false
  }
}
</script> 