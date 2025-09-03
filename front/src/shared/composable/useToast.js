import { ref } from 'vue'

const toasts = ref([])
let seq = 0

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
    if (duration > 0) {
      setTimeout(() => dismiss(id), duration)
    }
    return id
  }

  function dismiss(id) {
    toasts.value = toasts.value.filter((t) => t.id !== id)
  }

  function clear() {
    toasts.value = []
  }
  return { toasts, show, dismiss, clear }
}
