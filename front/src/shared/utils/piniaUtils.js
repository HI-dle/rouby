const PINIA_STATE_KEY = 'pinia-state'

export function getPiniaStorage() {
  try {
    const raw =
      localStorage.getItem(PINIA_STATE_KEY) ||
      sessionStorage.getItem(PINIA_STATE_KEY)
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

    localStorage.removeItem(PINIA_STATE_KEY)
    sessionStorage.removeItem(PINIA_STATE_KEY)

    storage.setItem(
      PINIA_STATE_KEY,
      JSON.stringify({ staySignedIn: staySignedIn }),
    )
  } catch (e) {
    console.error('Failed to set Pinia storage:', e)
  }
}
