export function useDatePickerGestures({ isMonthly, prevWeek, nextWeek, prevMonth, nextMonth }) {
  let touchStartX = 0
  let touchStartY = 0

  const onTouchStart = (e) => {
    touchStartX = e.changedTouches[0].clientX
    touchStartY = e.changedTouches[0].clientY
  }

  const onTouchEnd = (e) => {
    const diffX = e.changedTouches[0].clientX - touchStartX
    const diffY = e.changedTouches[0].clientY - touchStartY

    if (Math.abs(diffX) > Math.abs(diffY) && Math.abs(diffX) > 50) {
      isMonthly.value
        ? diffX > 0
          ? prevMonth()
          : nextMonth()
        : diffX > 0
          ? prevWeek()
          : nextWeek()
    } else if (Math.abs(diffY) > 30) {
      isMonthly.value = diffY > 0
    }
  }

  return { onTouchStart, onTouchEnd }
}
