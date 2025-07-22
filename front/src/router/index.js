import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: () => import('@/layouts/DefaultLayout.vue'),
      redirect: '/routine/daily',
      children: [
        {
          path: '/user',
          component: () => import('@/features/user/views/UserLayout.vue'),
          children: [
            {
              path: 'password/reset',
              name: 'password-reset',
              component: () =>
                import('@/features/user/views/MyPageResetPasswordView.vue'),
            },
          ],
        },
        {
          path: 'schedule',
          component: () =>
            import('@/features/schedule/views/ScheduleLayout.vue'),
          redirect: '/schedule/daily',
          children: [
            {
              path: 'create',
              name: 'schedule-create',
              component: () =>
                import('@/features/schedule/views/CreateScheduleView.vue'),
            },
            {
              path: 'daily',
              name: 'schedule-daily',
              component: () =>
                import('@/features/schedule/views/DailyScheduleView.vue'),
            },
            {
              path: '/schedule/:id/:date',
              name: 'ScheduleDetail',
              component: () =>
                import('@/features/schedule/views/ModifyScheduleView.vue'),
              props: (route) => ({
                id: Number(route.params.id),
                date: route.params.date,
              }),
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
              component: () =>
                import('@/features/routine/views/DailyRoutineView.vue'),
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
    {
      path: '/auth',
      component: () => import('@/layouts/HeaderOnlyLayout.vue'),
      children: [
        {
          path: 'signup',
          name: 'signup',
          component: () => import('@/features/auth/views/SignupView.vue'),
        },
        {
          path: 'login',
          name: 'login',
          component: () => import('@/features/auth/views/LoginView.vue'),
        },
        {
          path: 'password/find',
          name: 'password-find',
          component: () => import('@/features/auth/views/FindPasswordView.vue'),
        },
        {
          path: 'password/reset/token',
          name: 'password-reset-token',
          component: () =>
            import('@/features/auth/views/ResetPasswordView.vue'),
          // props: route => ({ token: route.query.token }),
        },
      ],
    },
    {
      path: '/onboarding',
      component: () => import('@/layouts/HeaderOnlyLayout.vue'),
      children: [
        {
          path: 'nickname-setting',
          name: 'nickname-setting',
          component: () =>
            import('@/features/onboard/views/OnboardNicknameSettingView.vue'),
        },
        {
          path: 'health-check',
          name: 'health-check',
          component: () =>
            import('@/features/onboard/views/OnboardHealthCheckView.vue'),
        },
        {
          path: 'profile-setting',
          name: 'profile-setting',
          component: () =>
            import('@/features/onboard/views/OnboardProfileSettingView.vue'),
        },
        {
          path: 'start-date-setting',
          name: 'start-date-setting',
          component: () => import('@/features/onboard/views/OnboardDateStartSettingView.vue'),
        },
        {
          path: 'end-date-setting',
          name: 'end-date-setting',
          component: () => import('@/features/onboard/views/OnboardDateEndSettingView.vue'),
        },
        {
          path: 'speech-setting',
          name: 'speech-setting',
          component: () => import('@/features/onboard/views/OnboardSpeechSettingView.vue'),
        },
        {
          path: 'alarm-setting',
          name: 'alarm-setting',
          component: () => import('@/features/onboard/views/OnboardAlarmSettingView.vue'),
        },
        {
          path: 'calender-setting',
          name: 'calender-setting',
          component: () => import('@/features/onboard/views/OnboardCalendarView.vue'),
        },
        {
          path: 'setting/rouby',
          name: 'rouby-setting',
          component: () => import('@/features/user/views/MyPageRoubySettingView.vue'),
        },
      ],
    },
  ],
})

export default router
