import Cookies from 'js-cookie'

const CsrfTokenKey = 'XSRF-TOKEN'
const AuthKey = 'AUTH'

export function getAuthState() {
  return Cookies.get(AuthKey)
}
export function removeAuthCookie() {
  return Cookies.remove(AuthKey)
}
export function getCsrfToken() {
  return Cookies.get(CsrfTokenKey)
}

export function setToken(token) {
  return Cookies.set(CsrfTokenKey, token)
}

export function removeToken() {
  return Cookies.remove(CsrfTokenKey)
}
