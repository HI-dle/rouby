import { ref, onBeforeUnmount } from 'vue'

export function useTimer({
  initialSeconds = 300,
  interval       = 1000,
  autoStart      = false,
  onExpire       = () => {}
} = {}) {
  const timeLeft    = ref(initialSeconds)
  const timerId     = ref(null)
  const isAvailable = ref(!autoStart)

  function tick() {
    if (timeLeft.value > 0) {
      timeLeft.value -= 1
    } else {
      stop()
      onExpire()
    }
  }

  function start() {
    if (timerId.value !== null) return
    isAvailable.value = false
    if (timeLeft.value <= 0) {
      timeLeft.value = initialSeconds
    }
    timerId.value = setInterval(tick, interval)
  }

  function stop() {
    if (timerId.value !== null) {
      clearInterval(timerId.value)
      timerId.value = null
    }
    isAvailable.value = true
  }

  function reset(newSeconds) {
    stop()
    timeLeft.value = typeof newSeconds === 'number'
      ? newSeconds
      : initialSeconds
  }

  function formatted() {
    const m = Math.floor(timeLeft.value / 60)
    const s = timeLeft.value % 60
    return `${m}:${String(s).padStart(2, '0')}`
  }

  onBeforeUnmount(() => stop())

  if (autoStart) start()

  return {
    timeLeft,
    isAvailable,
    start,
    stop,
    reset,
    formatted
  }
}
