/** @type {import('tailwindcss').Config} */
export default {
  content: ['index.html', 'src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        bodyText: '#6667D0', // 본문 텍스트 컬러
        placeholderColor: '#9F7AEA',
        contentColor: '#5A67D8',
      }
    },
  },
  plugins: [],
}
