<template>
  <div>
    <!-- Profile Header -->
    <BaseCard class="q-mb-lg">
      <template #header>
        <div class="text-center">
          <div class="text-h4 q-mb-md">My Profile</div>
          
          <div v-if="user" class="row justify-center items-center q-gutter-lg">
            <q-avatar size="80px" color="primary" text-color="white" class="text-h3">
              {{ user.username.charAt(0).toUpperCase() }}
            </q-avatar>
            <div class="text-left">
              <div class="text-h5" data-cy="user-username">{{ user.username }}</div>
              <div class="text-subtitle1 text-grey-7" data-cy="user-email">{{ user.email }}</div>
            </div>
          </div>
          
          <LoadingSpinner 
            v-else 
            size="30px"
            message="Loading user profile..."
          />
        </div>
      </template>
    </BaseCard>

    <div v-if="user">
      <!-- Statistics Section -->
      <BaseCard class="q-mb-lg" title="My Statistics">
        <div class="row q-gutter-md">
          <q-card class="col-12 col-sm-6">
            <q-card-section class="text-center">
              <div class="text-h3 text-primary">{{ userReviews.length }}</div>
              <div class="text-subtitle1">Reviews written</div>
            </q-card-section>
          </q-card>
          <q-card class="col-12 col-sm-6">
            <q-card-section class="text-center">
              <div class="text-h3 text-primary">{{ averageRating.toFixed(1) }}</div>
              <div class="text-subtitle1">Average rating</div>
            </q-card-section>
          </q-card>
        </div>
      </BaseCard>

      <!-- Reviews Section -->
      <BaseCard title="My Reviews">
        <div v-if="userReviews.length > 0">
          <q-list separator>
            <q-item v-for="review in userReviews" :key="review.id">
              <q-item-section>
                <q-item-label class="row items-center justify-between">
                  <span class="text-weight-medium">
                    {{ review.book?.title || 'Unknown book' }}
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
                <q-item-label caption class="text-grey-6">
                  {{ formatDate(review.createdAt) }}
                </q-item-label>
              </q-item-section>
            </q-item>
          </q-list>
        </div>
        
        <EmptyState 
          v-else
          icon="rate_review"
          title="You haven't written any reviews yet"
        >
          <template #action>
            <q-btn 
              color="primary" 
              label="Browse Books" 
              icon="library_books"
              class="q-mt-md"
              @click="$emit('browseBooks')"
            />
          </template>
        </EmptyState>
      </BaseCard>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { apiService } from '../services/api'
import BaseCard from './ui/BaseCard.vue'
import RatingDisplay from './ui/RatingDisplay.vue'
import LoadingSpinner from './ui/LoadingSpinner.vue'
import EmptyState from './ui/EmptyState.vue'

interface User {
  id: number
  username: string
  email: string
}

interface Book {
  id: number
  title: string
  author: string
}

interface Review {
  id: number
  rating: number
  comment?: string
  createdAt: string
  book?: Book
  user?: User
}

const user = ref<User | null>(null)
const userReviews = ref<Review[]>([])
const loading = ref(true)

const emit = defineEmits<{
  browseBooks: []
}>()

const averageRating = computed(() => {
  if (userReviews.value.length === 0) return 0
  const sum = userReviews.value.reduce((acc, review) => acc + review.rating, 0)
  return sum / userReviews.value.length
})

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

const fetchUserProfile = async () => {
  try {
    // For demo purposes, we'll use a hardcoded user ID
    // In a real app, this would come from authentication
    const userId = 1
    
    const [userResponse, reviewsResponse] = await Promise.all([
      apiService.getUser(userId),
      apiService.getUserReviews(userId)
    ])

    if (userResponse.error) {
      console.error('Error fetching user:', userResponse.error)
    } else if (userResponse.data) {
      user.value = userResponse.data
    }

    if (reviewsResponse.error) {
      console.error('Error fetching reviews:', reviewsResponse.error)
    } else if (reviewsResponse.data) {
      userReviews.value = reviewsResponse.data
    }
  } catch (error) {
    console.error('Error fetching user profile:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchUserProfile()
})
</script> 