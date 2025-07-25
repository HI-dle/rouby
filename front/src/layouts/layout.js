export const setVH = () => {
  const vh = window.innerHeight * 0.01
  document.documentElement.style.setProperty('--vh', `${vh}px`)
}

export const initVH = () => {
  setVH()
  window.addEventListener('resize', setVH)
}
