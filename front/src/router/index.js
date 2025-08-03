import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: () => import('@/layouts/DefaultLayout.vue'),
      redirect: '/schedule/daily/list',
      children: [
        {
          path: 'user',
          component: () => import('@/features/user/views/UserLayout.vue'),
          children: [
            {
              path: 'mypage',
              name: 'mypage',
              component: () => import('@/features/user/views/MyPageView.vue'),
            },
            {
              path: 'password',
              name: 'password-reset',
              component: () =>
                import('@/features/user/views/MyPageResetPasswordView.vue'),
            },
            {
              path: 'rouby-setting',
              name: 'rouby-setting',
              component: () =>
                import('@/features/user/views/MyPageRoubySettingView.vue'),
            },
          ],
        },
        {
          path: 'schedule',
          component: () =>
            import('@/features/schedule/views/ScheduleLayout.vue'),
          redirect: '/schedule/daily/list',
          children: [
            {
              path: 'daily/list',
              name: 'schedule-daily',
              component: () =>
                import('@/features/schedule/views/DailyScheduleListView.vue'),
            },
            {
              path: 'monthly',
              name: 'schedule-monthly',
              component: () =>
                import(
                  '@/features/schedule/views/MonthlyScheduleCalendarView.vue'
                ),
            },
            {
              path: 'create',
              name: 'schedule-create',
              component: () =>
                import('@/features/schedule/views/CreateScheduleView.vue'),
              props: (route) => ({
                start: route.query.start,
                end: route.query.end,
                allDay: route.query.allDay,
              }),
            },

            {
              path: ':id/:date',
              name: 'schedule-modify',
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
          path: 'routine-task',
          component: () =>
            import('@/features/routine-task/views/RoutineTaskLayout.vue'),
          redirect: '/routine-task/daily',
          children: [
            {
              path: 'create',
              name: 'routine-task-create',
              component: () =>
                import(
                  '@/features/routine-task/views/CreateRoutineTaskView.vue'
                ),
            },
          ],
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
            import('@/features/onboard/views/OnboardnicknameSettingView.vue'),
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
          component: () =>
            import('@/features/onboard/views/OnboardDateStartSettingView.vue'),
        },
        {
          path: 'end-date-setting',
          name: 'end-date-setting',
          component: () =>
            import('@/features/onboard/views/OnboardDateEndSettingView.vue'),
        },
        {
          path: 'speech-setting',
          name: 'speech-setting',
          component: () =>
            import('@/features/onboard/views/OnboardSpeechSettingView.vue'),
        },
        {
          path: 'alarm-setting',
          name: 'alarm-setting',
          component: () =>
            import('@/features/onboard/views/OnboardAlarmSettingView.vue'),
        },
        {
          path: 'calender-setting',
          name: 'calender-setting',
          component: () =>
            import('@/features/onboard/views/OnboardCalendarView.vue'),
        },
      ],
    },
  ],
})

export default router
