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
