<script setup lang="ts">
import TheWelcome from '../components/TheWelcome.vue'
</script>

<template>
  <div class="home">
    <!-- Hero Section -->
    <q-card class="hero-section q-mb-xl">
      <q-card-section class="text-center q-pa-xl">
        <div class="text-h2 q-mb-md text-primary">Welcome to BookReviewer</div>
        <div class="text-h5 text-grey-7 q-mb-lg">
          Discover and review your favorite books
        </div>
        <q-btn 
          color="primary" 
          size="lg"
          label="Browse Books" 
          icon="library_books"
          @click="scrollToBooks"
        />
      </q-card-section>
    </q-card>

    <!-- Book Catalog Section -->
    <div ref="bookSection">
      <div class="text-h4 q-mb-lg">Book Catalog</div>
      <BookList @book-selected="handleBookSelection" />
    </div>

    <!-- Features Section -->
    <q-card class="q-mt-xl">
      <q-card-section>
        <div class="text-h4 q-mb-lg text-center">Why Choose BookReviewer?</div>
        <div class="row q-gutter-lg">
          <div class="col-12 col-md-4 text-center">
            <q-icon name="rate_review" size="4em" color="primary" class="q-mb-md" />
            <div class="text-h6 q-mb-sm">Write Reviews</div>
            <div class="text-body2 text-grey-7">
              Share your thoughts and help other readers discover great books
            </div>
          </div>
          <div class="col-12 col-md-4 text-center">
            <q-icon name="search" size="4em" color="primary" class="q-mb-md" />
            <div class="text-h6 q-mb-sm">Discover Books</div>
            <div class="text-body2 text-grey-7">
              Find your next favorite read from our curated collection
            </div>
          </div>
          <div class="col-12 col-md-4 text-center">
            <q-icon name="people" size="4em" color="primary" class="q-mb-md" />
            <div class="text-h6 q-mb-sm">Join Community</div>
            <div class="text-body2 text-grey-7">
              Connect with fellow book lovers and share recommendations
            </div>
          </div>
        </div>
      </q-card-section>
    </q-card>
  </div>
</template>
