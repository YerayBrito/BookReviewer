import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export interface Book {
  id: number
  title: string
  author: string
  reviews?: Review[]
}

export interface Review {
  id: number
  rating: number
  comment?: string
  user?: User
  book?: Book
}

export interface User {
  id: number
  username: string
  email: string
}

export const useBookStore = defineStore('book', () => {
  const books = ref<Book[]>([])
  const selectedBook = ref<Book | null>(null)
  const loading = ref(false)
  const error = ref('')

  const booksWithStats = computed(() => {
    return books.value.map(book => ({
      ...book,
      averageRating: book.reviews && book.reviews.length > 0
        ? book.reviews.reduce((acc, review) => acc + review.rating, 0) / book.reviews.length
        : null,
      reviewsCount: book.reviews?.length || 0
    }))
  })

  const fetchBooks = async () => {
    loading.value = true
    error.value = ''
    
    try {
      const response = await fetch('http://localhost:8080/api/books')
      if (response.ok) {
        books.value = await response.json()
      } else {
        error.value = 'Failed to load books'
      }
    } catch (err) {
      error.value = `Network error: ${err instanceof Error ? err.message : 'Backend might not be running'}`
    } finally {
      loading.value = false
    }
  }

  const fetchBookById = async (id: number) => {
    loading.value = true
    error.value = ''
    
    try {
      const response = await fetch(`http://localhost:8080/api/books/${id}`)
      if (response.ok) {
        selectedBook.value = await response.json()
      } else {
        error.value = 'Failed to load book details'
      }
    } catch (err) {
      error.value = `Network error: ${err instanceof Error ? err.message : 'Backend might not be running'}`
    } finally {
      loading.value = false
    }
  }

  const addReview = async (bookId: number, userId: number, rating: number, comment?: string) => {
    try {
      const response = await fetch('http://localhost:8080/api/reviews', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          bookId,
          userId,
          rating,
          comment
        })
      })

      if (response.ok) {
        // Refresh the book details to show the new review
        await fetchBookById(bookId)
        return true
      } else {
        const errorData = await response.json()
        error.value = errorData.message || 'Failed to submit review'
        return false
      }
    } catch (err) {
      error.value = `Network error: ${err instanceof Error ? err.message : 'Backend might not be running'}`
      return false
    }
  }

  const clearError = () => {
    error.value = ''
  }

  const setSelectedBook = (book: Book | null) => {
    selectedBook.value = book
  }

  return {
    books,
    selectedBook,
    loading,
    error,
    booksWithStats,
    fetchBooks,
    fetchBookById,
    addReview,
    clearError,
    setSelectedBook
  }
}) 