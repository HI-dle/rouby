export const dateTimeUtil = () => {
  const formatDateTime = (date, { type = 'datetime', noMins = false } = {}) => {
    const copy = new Date(date) // 원본 보호
    if (noMins) copy.setMinutes(0, 0)

    const yyyy = copy.getFullYear()
    const mm = String(copy.getMonth() + 1).padStart(2, '0')
    const dd = String(copy.getDate()).padStart(2, '0')
    const hh = String(copy.getHours()).padStart(2, '0')
    const mi = String(copy.getMinutes()).padStart(2, '0')

    const iso = `${yyyy}-${mm}-${dd}T${hh}:${mi}`
    return type === 'date' ? iso.slice(0, 10) : iso
  }

  const convertDateToDateTime = (dateStr, hours = 0) => {
    if (dateStr.includes('T')) return dateStr

    const hoursStr = String(hours).padStart(2, '0')
    return `${dateStr}T${hoursStr}:00`
  }

  const extractDate = (dateTimeStr) => {
    return dateTimeStr?.slice(0, 10) ?? ''
  }

  const extractTime = (dateTimeStr) => {
    return dateTimeStr?.slice(11) ?? ''
  }

  return {
    formatDateTime,
    convertDateToDateTime,
    extractDate,
    extractTime,
  }
}
