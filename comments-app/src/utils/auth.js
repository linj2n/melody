import Cookies from 'js-cookie'

const AuthKey = 'AUTH'

export function getAuthState () {
  return Cookies.get(AuthKey)
}
export function removeAuthCookie () {
  return Cookies.remove(AuthKey)
}

export function isAuthenticated () {
  return Cookies.get(AuthKey) && Cookies.get(AuthKey) === 'true'
}
