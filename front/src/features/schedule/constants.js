export const alarmOptions = [
  { value: 'NONE', label: '없음' },
  { value: 0, label: '일정 시간' },
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

export const repeatOptions = [
  { value: 'NONE', label: '안 함' },
  { value: 'DAILY', label: '매일' },
  { value: 'WEEKLY', label: '매주' },
  { value: 'BIWEEKLY', label: '2주마다' },
  { value: 'MONTHLY', label: '매월' },
  { value: 'YEARLY', label: '매년' },
]

export const errorMessage = {
  title: {
    notNull: '제목을 입력해주세요.',
  },
  period: {
    startNotNull: '시작일을 입력해주세요.',
    endNotNull: '종료일을 입력해주세요.',
    invalidRange: '시작일보다 종료일이 앞일 수 없습니다.',
  },
  routineStart: {
    reversed: '루틴 시작일은 종료일보다 나중일 수 없습니다.',
  },
}
