import { nextTick } from 'vue'

export const useAutoResize = () => {
  const autoResize = async (e) => {
    const el = e.target
    if (!el) return

    await nextTick()
    el.style.height = 'auto'
    el.style.height = `${el.scrollHeight}px`
  }

  return { autoResize }
}
