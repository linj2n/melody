import request from '@/utils/request'

export function initCsrfToken() {
  return request({
    url: '/api/blank',
    method: 'get'
  })
}
export function register(data) {
  return request({
    url: '/api/v1/account/registration',
    method: 'post',
    data
  })
}

export function checkUsernameOrEmailExistence(login, email) {
  return request({
    url: '/api/v1/account/existence',
    method: 'get',
    params: {
      login: login,
      email: email || ''
    }
  })
}
