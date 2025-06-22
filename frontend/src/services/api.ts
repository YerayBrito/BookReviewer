// API Service for Book Reviewer
// Dynamic API base URL configuration

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'

interface ApiResponse<T> {
  data?: T
  error?: string
}

interface Book {
  id: number
  title: string
  author: string
  reviews?: Review[]
  averageRating?: number
  reviewsCount?: number
}

interface Review {
  id: number
  rating: number
  comment?: string
  user?: User
  book?: Book
  createdAt: string
}

interface User {
  id: number
  username: string
  email: string
  reviews?: Review[]
  averageRating?: number
  reviewsCount?: number
}

class ApiService {
  private async makeRequest<T>(url: string, options: RequestInit = {}): Promise<ApiResponse<T>> {
    try {
      const response = await fetch(`${API_BASE_URL}${url}`, {
        headers: {
          'Content-Type': 'application/json',
          ...options.headers
        },
        ...options
      })

      if (!response.ok) {
        const errorText = await response.text()
        return { error: errorText }
      }

      const data = await response.json()
      return { data }
    } catch (error) {
      return { error: error instanceof Error ? error.message : 'Network error' }
    }
  }

  // Books API
  async getBooks(): Promise<ApiResponse<Book[]>> {
    return this.makeRequest<Book[]>('/books')
  }

  async getBook(id: number): Promise<ApiResponse<Book>> {
    return this.makeRequest<Book>(`/books/${id}`)
  }

  async createBook(book: { title: string; author: string }): Promise<ApiResponse<Book>> {
    return this.makeRequest<Book>('/books', {
      method: 'POST',
      body: JSON.stringify(book)
    })
  }

  async updateBook(id: number, book: { title: string; author: string }): Promise<ApiResponse<Book>> {
    return this.makeRequest<Book>(`/books/${id}`, {
      method: 'PUT',
      body: JSON.stringify(book)
    })
  }

  async deleteBook(id: number): Promise<ApiResponse<void>> {
    return this.makeRequest<void>(`/books/${id}`, {
      method: 'DELETE'
    })
  }

  async searchBooks(params: { title?: string; author?: string }): Promise<ApiResponse<Book[]>> {
    const searchParams = new URLSearchParams()
    if (params.title) searchParams.append('title', params.title)
    if (params.author) searchParams.append('author', params.author)
    
    return this.makeRequest<Book[]>(`/books/search?${searchParams.toString()}`)
  }

  // Reviews API
  async createReview(review: { bookId: number; userId: number; rating: number; comment?: string }): Promise<ApiResponse<Review>> {
    return this.makeRequest<Review>('/reviews', {
      method: 'POST',
      body: JSON.stringify(review)
    })
  }

  async getReviews(): Promise<ApiResponse<Review[]>> {
    return this.makeRequest<Review[]>('/reviews')
  }

  async getReview(id: number): Promise<ApiResponse<Review>> {
    return this.makeRequest<Review>(`/reviews/${id}`)
  }

  async updateReview(id: number, review: { rating: number; comment?: string }): Promise<ApiResponse<Review>> {
    return this.makeRequest<Review>(`/reviews/${id}`, {
      method: 'PUT',
      body: JSON.stringify(review)
    })
  }

  async deleteReview(id: number): Promise<ApiResponse<void>> {
    return this.makeRequest<void>(`/reviews/${id}`, {
      method: 'DELETE'
    })
  }

  async getReviewsByBook(bookId: number): Promise<ApiResponse<Review[]>> {
    return this.makeRequest<Review[]>(`/reviews/book/${bookId}`)
  }

  async getReviewsByUser(userId: number): Promise<ApiResponse<Review[]>> {
    return this.makeRequest<Review[]>(`/reviews/user/${userId}`)
  }

  // Users API
  async getUsers(): Promise<ApiResponse<User[]>> {
    return this.makeRequest<User[]>('/users')
  }

  async getUser(id: number): Promise<ApiResponse<User>> {
    return this.makeRequest<User>(`/users/${id}`)
  }

  async createUser(user: { username: string; email: string }): Promise<ApiResponse<User>> {
    return this.makeRequest<User>('/users', {
      method: 'POST',
      body: JSON.stringify(user)
    })
  }

  async updateUser(id: number, user: { username: string; email: string }): Promise<ApiResponse<User>> {
    return this.makeRequest<User>(`/users/${id}`, {
      method: 'PUT',
      body: JSON.stringify(user)
    })
  }

  async deleteUser(id: number): Promise<ApiResponse<void>> {
    return this.makeRequest<void>(`/users/${id}`, {
      method: 'DELETE'
    })
  }

  async getUserReviews(userId: number): Promise<ApiResponse<Review[]>> {
    return this.makeRequest<Review[]>(`/users/${userId}/reviews`)
  }

  async searchUsers(params: { username?: string; email?: string }): Promise<ApiResponse<User[]>> {
    const searchParams = new URLSearchParams()
    if (params.username) searchParams.append('username', params.username)
    if (params.email) searchParams.append('email', params.email)
    
    return this.makeRequest<User[]>(`/users/search?${searchParams.toString()}`)
  }
}

export const apiService = new ApiService() 