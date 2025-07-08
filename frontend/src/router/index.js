import { createRouter, createWebHistory } from 'vue-router'
import WelcomeView from '../views/WelcomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'welcome',
      component: WelcomeView,
    },
    {
      path: '/survey',
      name: 'survey',
      component: () => import('../views/SurveyView.vue'),
    },
    {
      path: '/completion',
      name: 'completion',
      component: () => import('../views/CompletionView.vue'),
    },
    {
      path: '/stats',
      name: 'stats',
      component: () => import('../views/StatsView.vue'),
    },
    {
      path: '/test-music',
      name: 'test-music',
      component: () => import('../views/TestMusicView.vue'),
    },
  ],
})

export default router
