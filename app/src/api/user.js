import qs from 'qs'
import request from '@/utils/request'

export function login(username, password) {
  const data = {
    'username': username,
    'password': password
  }
  return request({
    url: '/api/v1/account/authentication',
    method: 'post',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'
    },
    data: qs.stringify(data)
  })
}

export function getInfo() {
  return request({
    url: '/api/v1/account',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/api/v1/logout',
    method: 'post'
  })
}

export function validateVerificationCode(code) {
  return request({
    url: `/api/v1/account/verification_code/${code}/_validate`,
    method: 'post'
  })
}

export function requestToSendVerificationCode() {
  return request({
    url: '/api/v1/account/verification_code',
    method: 'post'
  })
}

export function changeEmail(email, code) {
  return request({
    url: '/api/v1/account/email',
    method: 'put',
    data: {
      email,
      code
    }
  })
}

export function changeUsername(username, code) {
  return request({
    url: '/api/v1/account/username',
    method: 'put',
    data: {
      username,
      code
    }
  })
}

export function changePassword(password, code) {
  return request({
    url: '/api/v1/account/password',
    method: 'put',
    data: {
      password,
      code
    }
  })
}
