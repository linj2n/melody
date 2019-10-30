import Cookies from 'js-cookie'

const AuthKey = 'AUTH'

export function getAuthState () {
  return Cookies.get(AuthKey)
}
export function removeAuthCookie () {
  return Cookies.remove(AuthKey)
}
