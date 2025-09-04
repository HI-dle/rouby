export function safeNavigate(rawUrl) {
  try {
    const u = new URL(rawUrl, location.origin)

    if (!/^https?:$/.test(u.protocol)) return

    const allowed = [location.origin]
    if (!allowed.includes(u.origin)) return
    location.href = u.href
  } catch {
    /* no-op */
  }
}
