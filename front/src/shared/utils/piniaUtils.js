export function getPiniaStorage() {
  try {
    const raw =
      localStorage.getItem('pinia-state') ||
      sessionStorage.getItem('pinia-state')
    const parsed = raw ? JSON.parse(raw) : {}
    const saved = parsed?.staySignedIn

    return saved ? localStorage : sessionStorage
  } catch (e) {
    console.warn('Pinia storage fallback to sessionStorage due to error:', e)
    return sessionStorage
  }
}

export function setPiniaStorage(staySignedIn) {
  try {
    const storage = staySignedIn ? localStorage : sessionStorage

    localStorage.removeItem('pinia-state')
    sessionStorage.removeItem('pinia-state')

    storage.setItem(
      'pinia-state',
      JSON.stringify({ staySignedIn: staySignedIn }),
    )
  } catch (e) {
    console.error('Failed to set Pinia storage:', e)
  }
}
