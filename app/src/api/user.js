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

export function validateResetKey(email, token) {
  return request({
    url: '/api/v1/account/resetKey/_validate',
    method: 'post',
    data: {
      email,
      token
    }
  })
}

export function requestToSendResetKey() {
  return request({
    url: '/api/v1/account/resetKey',
    method: 'post'
  })
}

export function changeEmail(email, token) {
  return request({
    url: '/api/v1/account/email',
    method: 'put',
    data: {
      'email': email,
      'token': 120502
    }
  })
}
