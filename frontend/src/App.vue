<script setup lang="ts">
import { ref } from 'vue'
import BookList from './components/BookList.vue'
import BookDetail from './components/BookDetail.vue'
import UserProfile from './components/UserProfile.vue'
import './assets/global.css'

interface Review {
  id: number
  rating: number
  comment?: string
  user?: {
    id: number
    username: string
    email: string
  }
}

interface Book {
  id: number
  title: string
  author: string
  reviews?: Review[]
}

const currentView = ref<'books' | 'book-detail' | 'profile'>('books')
const selectedBook = ref<Book | null>(null)

const handleBookSelected = (book: Book) => {
  selectedBook.value = book
  currentView.value = 'book-detail'
}

const handleBackToBooks = () => {
  currentView.value = 'books'
  selectedBook.value = null
}

const handleBrowseBooks = () => {
  currentView.value = 'books'
}
</script>

<template>
  <div id="app" data-cy="app-container">
    <q-layout view="hHh lpR fFf">
      <!-- Header -->
      <q-header elevated class="bg-primary text-white" data-cy="app-header">
        <q-toolbar>
          <q-toolbar-title data-cy="app-title">
            <q-icon name="menu_book" class="q-mr-sm" />
            Book Reviewer
          </q-toolbar-title>
          
          <!-- Navigation Menu -->
          <q-tabs 
            v-model="currentView" 
            class="text-white"
            data-cy="nav-menu"
          >
            <q-tab 
              name="books" 
              label="Books" 
              icon="library_books"
              data-cy="nav-books"
              :class="{ 'bg-secondary': currentView === 'books' }"
              @click="currentView = 'books'"
            />
            <q-tab 
              name="profile" 
              label="My Profile" 
              icon="person"
              data-cy="user-profile-link"
              :class="{ 'bg-secondary': currentView === 'profile' }"
              @click="currentView = 'profile'"
            />
          </q-tabs>
        </q-toolbar>
      </q-header>

      <!-- Page Container -->
      <q-page-container data-cy="main-content">
        <q-page class="q-pa-md">
          <!-- Books View -->
          <div v-if="currentView === 'books'">
            <BookList @book-selected="handleBookSelected" />
          </div>
          
          <!-- Book Detail View -->
          <div v-else-if="currentView === 'book-detail' && selectedBook">
            <BookDetail 
              :book="selectedBook" 
              @back="handleBackToBooks"
              @review-submitted="handleBackToBooks"
            />
          </div>
          
          <!-- Profile View -->
          <div v-else-if="currentView === 'profile'">
            <UserProfile @browse-books="handleBrowseBooks" />
          </div>
        </q-page>
      </q-page-container>
    </q-layout>
  </div>
</template>
