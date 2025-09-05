import { ref } from 'vue'

// 싱글톤
const toasts = ref([])
let seq = 0
const timers = new Map()

export const useToast = () => {
  function show({
    title = '',
    message = '',
    variant = 'default',
    duration = 4000,
    onClick = () => {},
  }) {
    const id = ++seq
    const t = { id, title, message, variant, duration, onClick }
    toasts.value.push(t)

    if (Number.isFinite(duration) && duration > 0) {
      const h = setTimeout(() => {
        dismiss(id)
      }, duration)
      timers.set(id, h)
    }
    return id
  }

  function dismiss(id) {
    const h = timers.get(id)

    if (h !== undefined) {
      clearTimeout(h)
      timers.delete(id)
    }
    toasts.value = toasts.value.filter((t) => t.id !== id)
  }

  function clear() {
    timers.forEach((h) => clearTimeout(h))
    timers.clear()

    toasts.value = []
  }

  return { toasts, show, dismiss, clear }
}
