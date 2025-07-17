import { errorMessage, days, dayEnums } from './constants.js'
import {parseISO, eachDayOfInterval, differenceInCalendarDays, isAfter} from 'date-fns'

export function validateForm(form, errors) {
  let isValid = true
  errors.title = ''
  errors.startDate = ''
  errors.until = ''
  errors.time = ''
  errors.byDays = ''

  // 기본 유효성 검사
  if (!form.title?.trim()) {
    errors.title = errorMessage.title?.notNull ?? '제목은 필수입니다.'
    isValid = false
  }

  if (!form.startDate) {
    errors.startDate = errorMessage.startDate?.notNull ?? '시작일을 선택해주세요.'
    isValid = false
  }

  if (!form.until) {
    errors.until = errorMessage.until?.notNull ?? '종료일을 선택해주세요.'
    isValid = false
  }

  if (!form.time) {
    errors.time = errorMessage.time?.notNull ?? '시간을 입력해주세요.'
    isValid = false
  }
  if (!form.byDays || form.byDays.length === 0) {
    errors.byDays = errorMessage.byDays?.notEmpty ?? '반복 요일을 1개 이상 선택해주세요.'
    isValid = false
  }

  if (form.startDate && form.until) {
    const start = parseISO(form.startDate)
    const end = parseISO(form.until)

    if (isAfter(start, end)) {
      errors.until = '종료일은 시작일보다 이후여야 합니다.'
      isValid = false
    }

    if (form.byDays?.length > 0) {
      const daysBetween = eachDayOfInterval({ start, end })
      const rangeLength = differenceInCalendarDays(end, start) + 1

      // 7일 미만일 때만 요일 유효성 검사
      if (rangeLength < 7) {
        const validEnumDays = new Set(
          daysBetween.map((date) => {
            const jsDay = date.getDay() // 0 = 일요일, ..., 6 = 토요일
            const converted = (jsDay + 6) % 7 // 월요일=0, 일요일=6
            return dayEnums[converted]        // → 'MO', 'TU', ...
          })
        )

        const invalidDays = form.byDays.filter((day) => !validEnumDays.has(day))

        if (invalidDays.length > 0) {
          const invalidNames = invalidDays.map((d) => {
            const idx = dayEnums.indexOf(d)
            return days[idx] ?? d
          }).join(', ')

          errors.byDays = `선택된 요일 중 ${invalidNames} 은(는) ${form.startDate} ~ ${form.until} 범위에 포함되지 않습니다.`
          isValid = false
        }
      }
    }
  }


  return isValid
}
