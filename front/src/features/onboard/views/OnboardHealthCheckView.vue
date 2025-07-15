<!-- views/OnboardingHealthCheckPage.vue 등 -->
<template>
  <div class="main-container">
    <div class="sub-main-container">
      <!-- 키워드 입력 폼 -->
<!--      아래 주석은 만약 백엔드에서 키워드를 유동적으로 받아온다면 적용하게끔 하는 거로 아래 폼에 추가하면 됩니다.-->
<!--      지금은 백에서 값이 없더라도 디폴트 값을 넣게끔 한게 있어용!-->
<!--      :example-keywords="exampleKeywords"-->
      <HealthKeywordForm
        :user-name="userName"
      />

      <!-- 다음 단계 이동 -->
      <div class="w-full mt-10 pt-10 text-center">
        <RouterLink
          to="/다음페이지경로"
          class="text-#6667D07A underline hover:text-indigo-400"
        >
          다음 단계로
        </RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import HealthKeywordForm from '@/features/onboard/Components/HealthKeywordForm.vue'

const userName = ref('하이들') // 서버에서 받아오는 값
const exampleKeywords = ref([]) //유동적으로 받아오기 가능

</script>
