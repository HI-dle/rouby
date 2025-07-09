//yyyy-mm-ddThh:mi 포맷

export const formatDateTime = (date, { type = 'datetime', noMins = false } = {}) => {
  const copy = new Date(date) // 원본 보호

  if (noMins) copy.setMinutes(0, 0)

  const yyyy = copy.getFullYear()
  const mm = String(copy.getMonth() + 1).padStart(2, '0')
  const dd = String(copy.getDate()).padStart(2, '0')
  const hh = String(copy.getHours()).padStart(2, '0')
  const mi = String(copy.getMinutes()).padStart(2, '0')

  const local = `${yyyy}-${mm}-${dd}T${hh}:${mi}`
  return type === 'date' ? local.slice(0, 10) : local
}

export const toDate = (dateTimeStr) => {
  return dateTimeStr?.slice(0, 10)
}

export const toTime = (dateTimeStr) => {
  return dateTimeStr?.slice(11, dateTimeStr.length)
}

export const toDateTime = (dateStr, hours) => {
  if (dateStr >= 10) return dateStr
  const hoursStr = String(hours).padStart(2, '0')
  return `${dateStr}T${hoursStr}:00`
}
