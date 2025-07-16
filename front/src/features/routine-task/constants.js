export const errorMessage = {
  title: {
    notNull: '제목을 입력해주세요.',
  },
  startDate: {
    notNull: '시작일을 선택해주세요.',
  },
  until: {
    notNull: '종료일을 선택해주세요.',
  },
  time: {
    notNull: '시간을 선택해주세요.',
  },
  byDays: {
    notEmpty: '반복 요일을 1개 이상 선택해주세요.',
  },
}

export const days = ['월', '화', '수', '목', '금', '토', '일']

export const typeOptions = [
  { value: 'CHECK', label: '체크 박스' },
  { value: 'MINUTES', label: '시간' },
  { value: 'COUNT', label: '횟수' },
]

export const alarmOptions = [
  { value: 'NONE', label: '없음' },
  { value: 5, label: '5분 전' },
  { value: 10, label: '10분 전' },
  { value: 15, label: '15분 전' },
  { value: 30, label: '30분 전' },
  { value: 60, label: '1시간 전' },
  { value: 120, label: '2시간 전' },
  { value: 1440, label: '1일 전' },
  { value: 2880, label: '2일 전' },
  { value: 10080, label: '1주 전' },
]
