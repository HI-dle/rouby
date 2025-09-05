export function safeNavigate(rawUrl, allowedOrigins = [location.origin]) {
  try {
    const u = new URL(rawUrl, location.origin)

    if (!/^https?:$/.test(u.protocol)) return

    const allowed = allowedOrigins
    if (!allowed.includes(u.origin)) return
    location.href = u.href
  } catch {
    /* no-op */
  }
}
