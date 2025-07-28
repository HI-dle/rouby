import { format } from 'date-fns'
import { ko } from 'date-fns/locale'
import { isValid, parseISO, isSameDay, addDays } from 'date-fns'

export const formatDateTime = (
  date,
  { type = 'datetime', noMins = false } = {},
) => {
  if (!date) return ''

  const copy = new Date(date)
  if (isNaN(copy.getTime())) return ''

  if (noMins) copy.setMinutes(0, 0)

  const patterns = {
    date: 'yyyy-MM-dd',
    time: 'HH:mm',
    datetime: "yyyy-MM-dd'T'HH:mm",
  }

  return format(copy, patterns[type] || patterns.datetime)
}

export const convertDateToDateTime = (dateStr, hours = 0) => {
  if (dateStr.includes('T')) return dateStr

  const hoursStr = String(hours).padStart(2, '0')
  return `${dateStr}T${hoursStr}:00`
}

export const extractDate = (dateTimeStr) => {
  return dateTimeStr?.slice(0, 10) ?? ''
}

export const extractTime = (dateTimeStr) => {
  return dateTimeStr?.slice(11) ?? ''
}

export const formatKoreanDatetime = (isoString) => {
  if (!isoString) return ''
  const date = new Date(isoString)
  if (isNaN(date)) return ''

  return format(date, 'yyyy.MM.dd a hh:mm', { locale: ko })
}

export const formatDateHeader = (dateString) => {
  const date = parseISO(dateString)
  const today = new Date()

  if (!isValid(date)) return ''

  const weekday = new Intl.DateTimeFormat('ko-KR', { weekday: 'long' }).format(date)
  const month = date.getMonth() + 1
  const day = date.getDate()

  if (isSameDay(date, today)) {
    return `오늘, ${month}월 ${day}일 ${weekday}`
  }

  if (isSameDay(date, addDays(today, 1))) {
    return `내일, ${month}월 ${day}일 ${weekday}`
  }

  if (isSameDay(date, addDays(today, -1))) {
    return `어제, ${month}월 ${day}일 ${weekday}`
  }

  return `${month}월 ${day}일 ${weekday}`
}
