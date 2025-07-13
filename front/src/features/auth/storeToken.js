
export function storeToken(token, staySignedIn) {
  if (staySignedIn) {
    localStorage.setItem('token', token);
  } else {
    sessionStorage.setItem('token', token);
  }
}

export function getStoredToken() {
  return localStorage.getItem('token') || sessionStorage.getItem('token');
}
