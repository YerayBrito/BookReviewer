<template>
  <q-card class="review-form">
    <q-card-section>
      <div class="text-h5 q-mb-md">Write a Review</div>
      
      <q-form @submit="submitReview" class="q-gutter-md">
        <!-- Book Selection -->
        <div v-if="books.length > 0">
          <q-select
            v-model="selectedBook"
            :options="books"
            option-label="title"
            option-value="id"
            label="Select a book to review"
            filled
            :rules="[val => !!val || 'Please select a book']"
          >
            <template v-slot:option="scope">
              <q-item v-bind="scope.itemProps">
                <q-item-section>
                  <q-item-label>{{ scope.opt.title }}</q-item-label>
                  <q-item-label caption>{{ scope.opt.author }}</q-item-label>
                </q-item-section>
              </q-item>
            </template>
          </q-select>
        </div>
        
        <div v-else class="text-center q-pa-md">
          <q-icon name="library_books" size="3em" color="grey-5" class="q-mb-sm" />
          <div class="text-grey-7">No books available for review</div>
        </div>

        <!-- Rating -->
        <div>
          <label class="text-weight-medium">Rating:</label>
          <div class="q-mt-sm">
            <q-rating 
              v-model="review.rating" 
              size="2em"
              color="amber"
              :rules="[val => val > 0 || 'Please provide a rating']"
            />
            <span class="q-ml-sm text-caption">{{ review.rating }}/5 stars</span>
          </div>
        </div>
        
        <!-- Comment -->
        <q-input
          v-model="review.comment"
          type="textarea"
          label="Your review (optional)"
          rows="4"
          maxlength="500"
          counter
          filled
          placeholder="Share your thoughts about this book. What did you like or dislike? Would you recommend it to others?"
        />
        
        <!-- Submit Button -->
        <div class="row justify-end">
          <q-btn 
            unelevated 
            label="Submit Review" 
            type="submit"
            color="primary"
            :loading="submitting"
            :disable="!selectedBook || review.rating === 0"
          />
        </div>
      </q-form>
    </q-card-section>
  </q-card>
</template>

<script setup lang="ts">
import { ref } from 'vue'

interface Props {
  bookId: number
  userId: number
}

const props = defineProps<Props>()

const emit = defineEmits<{
  submitted: []
  cancel: []
}>()

const selectedBook = ref(null)
const review = ref({ rating: 0, comment: '' })
const submitting = ref(false)

const submitReview = async () => {
  if (!selectedBook.value) return

  submitting.value = true

  try {
    const response = await fetch('http://localhost:8080/api/reviews', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        bookId: props.bookId,
        userId: props.userId,
        rating: review.value.rating,
        comment: review.value.comment.trim()
      })
    })

    if (response.ok) {
      // Reset form
      selectedBook.value = null
      review.value = { rating: 0, comment: '' }
      emit('submitted')
    } else {
      const errorData = await response.json()
      alert(`Failed to submit review: ${errorData.message || 'Unknown error'}`)
    }
  } catch (error) {
    alert('Network error: Backend might not be running')
  } finally {
    submitting.value = false
  }
}
</script> 