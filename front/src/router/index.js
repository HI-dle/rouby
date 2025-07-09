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
          component: () => import('@/features/schedule/views/ScheduleLayout.vue'),
          redirect: '/schedule/daily',
          children: [
            // {
            //   path: 'create',
            //   name: 'schedule-create',
            //   component: () => import('@/features/schedule/views/CreateScheduleView.vue'),
            // },
            {
              path: 'daily',
              name: 'schedule-daily',
              component: () => import('@/features/schedule/views/DailyScheduleView.vue'),
            },
          ],
        },
        {
          path: 'routine',
          component: () => import('@/features/routine/views/RoutineLayout.vue'),
          redirect: '/routine/daily',
          children: [
            {
              path: 'daily',
              name: 'routine-daily',
              component: () => import('@/features/routine/views/DailyRoutineView.vue'),
            },
          ],
        },
        {
        path: 'routine-task',
        component: () => import('@/features/routine-task/views/RoutineTaskLayout.vue'),
        redirect: '/routine/daily',
        children: [
            {
              path: 'create',
              name: 'routine-task-create',
              component: () => import('@/features/routine-task/views/CreateRoutineTaskView.vue'),
            },
          ],
        },
        {
          path: 'mypage',
          name: 'mypage',
          component: () => import('@/features/user/views/MypageView.vue'),
        },
      ],
    },
  ],
})

export default router
