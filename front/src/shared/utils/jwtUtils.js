export function decodeJwt(token) {
  try {
    if (!token) return null

    const payload = token.split('.')[1]
    if (!payload) return null

    const decoded = atob(payload.replace(/-/g, '+').replace(/_/g, '/'))
    return JSON.parse(decoded)
  } catch (e) {
    console.error('JWT 디코딩 실패:', e)
    return null
  }
}

export function getTokenExpiration(token) {
  const decoded = decodeJwt(token)
  return decoded?.exp ? decoded.exp * 1000 : null
}

export function isAccessTokenExpired(token) {
  const exp = getTokenExpiration(token)
  if (!exp) return true
  return Date.now() >= exp
}
