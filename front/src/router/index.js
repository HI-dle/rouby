import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: () => import('@/layouts/DefaultLayout.vue'),
      children: [
        {
          path: 'schedule',
          component: () => import('@/layouts/ScheduleLayout.vue'),
          redirect: '/schedule/daily',
          children: [
            // {
            //   path: 'create',
            //   name: 'schedule-create',
            //   component: () => import('@/views/schedule/CreateScheduleView.vue'),
            // },
            {
              path: 'daily',
              name: 'schedule-daily',
              component: () => import('@/views/schedule/DailyScheduleView.vue'),
            },
          ],
        },
        {
          path: 'routine',
          component: () => import('@/layouts/RoutineLayout.vue'),
          redirect: '/routine/daily',
          children: [
            {
              path: 'daily',
              name: 'routine-daily',
              component: () => import('@/views/routine/DailyRoutineView.vue'),
            },
          ],
        },
        {
          path: 'mypage',
          name: 'mypage',
          component: () => import('../views/user/MypageView.vue'),
        },
      ],
    },
  ],
})

export default router
