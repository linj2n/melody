import request from '@/utils/request'

export function getToken() {
  return request({
    url: '/api/v1/qiniu/upload-token',
    method: 'get'
  })
}

export function fecthUploadUrl() {
  return request({
    url: '/api/v1/qiniu/upload-url',
    method: 'get'
  })
}
