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

  const onTouchStart = (e) => {
    if (!e.changedTouches || e.changedTouches.length === 0) return

    touchStartX = e.changedTouches[0].clientX
    touchStartY = e.changedTouches[0].clientY
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

  return { onTouchStart, onTouchEnd }
}
