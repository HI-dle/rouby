const HORIZONTAL_THRESHOLD = 50
const VERTICAL_THRESHOLD = 30

export function useDatePickerGestures({
  isMonthly,
  prevWeek,
  nextWeek,
  prevMonth,
  nextMonth,
}) {
  let touchStartX = 0
  let touchStartY = 0

  let gestureHandled = false

  const onTouchStart = (e) => {
    if (!e.changedTouches || e.changedTouches.length === 0) return
    const touch = e.changedTouches[0]
    touchStartX = touch.clientX
    touchStartY = touch.clientY
    gestureHandled = false
  }

  const onTouchMove = (e) => {
    if (!e.changedTouches || e.changedTouches.length === 0 || gestureHandled)
      return

    const touch = e.changedTouches[0]
    const diffX = touch.clientX - touchStartX
    const diffY = touch.clientY - touchStartY

    if (
      Math.abs(diffY) > VERTICAL_THRESHOLD &&
      Math.abs(diffY) > Math.abs(diffX)
    ) {
      e.preventDefault()
      gestureHandled = true
    }
  }

  const onTouchEnd = (e) => {
    if (!e.changedTouches || e.changedTouches.length === 0) return

    const diffX = e.changedTouches[0].clientX - touchStartX
    const diffY = e.changedTouches[0].clientY - touchStartY

    if (
      Math.abs(diffX) > Math.abs(diffY) &&
      Math.abs(diffX) > HORIZONTAL_THRESHOLD
    ) {
      if (isMonthly.value) {
        diffX > 0 ? prevMonth() : nextMonth()
      } else {
        diffX > 0 ? prevWeek() : nextWeek()
      }
    } else if (Math.abs(diffY) > VERTICAL_THRESHOLD) {
      isMonthly.value = diffY > 0
    }
  }

  return { onTouchStart, onTouchMove, onTouchEnd }
}
