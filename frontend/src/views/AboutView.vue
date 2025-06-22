<template>
  <div class="about">
    <q-card>
      <q-card-section>
        <div class="text-h4 q-mb-lg">About BookReviewer</div>
        
        <div class="text-body1 q-mb-lg">
          <p>
            BookReviewer is a modern web application designed to help book lovers discover, 
            review, and share their favorite reads. Our platform provides a comprehensive 
            solution for managing your reading journey.
          </p>
          
          <p class="q-mt-md">
            Whether you're looking for your next great read or want to share your thoughts 
            about a book you've just finished, BookReviewer offers the tools and community 
            you need to enhance your reading experience.
          </p>
        </div>

        <div class="text-h5 q-mb-md">Key Features</div>
        <q-list>
          <q-item>
            <q-item-section avatar>
              <q-icon name="library_books" color="primary" />
            </q-item-section>
            <q-item-section>
              <q-item-label>Extensive Book Catalog</q-item-label>
              <q-item-label caption>
                Browse through thousands of books across all genres
              </q-item-label>
            </q-item-section>
          </q-item>
          
          <q-item>
            <q-item-section avatar>
              <q-icon name="rate_review" color="primary" />
            </q-item-section>
            <q-item-section>
              <q-item-label>Write Detailed Reviews</q-item-label>
              <q-item-label caption>
                Share your thoughts with ratings and detailed comments
              </q-item-label>
            </q-item-section>
          </q-item>
          
          <q-item>
            <q-item-section avatar>
              <q-icon name="people" color="primary" />
            </q-item-section>
            <q-item-section>
              <q-item-label>Community Features</q-item-label>
              <q-item-label caption>
                Connect with other readers and discover new recommendations
              </q-item-label>
            </q-item-section>
          </q-item>
          
          <q-item>
            <q-item-section avatar>
              <q-icon name="analytics" color="primary" />
            </q-item-section>
            <q-item-section>
              <q-item-label>Reading Statistics</q-item-label>
              <q-item-label caption>
                Track your reading progress and review history
              </q-item-label>
            </q-item-section>
          </q-item>
        </q-list>

        <div class="text-h5 q-mb-md q-mt-lg">Technology Stack</div>
        <div class="text-body2 text-grey-7">
          <p>
            Built with modern technologies including Vue 3, TypeScript, Quasar Framework, 
            and a robust backend powered by Quarkus and MariaDB. Our application is designed 
            for performance, scalability, and an excellent user experience.
          </p>
        </div>
      </q-card-section>
    </q-card>
  </div>
</template>

<style>
@media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
</style>
