import request from '@/utils/request'

export function fetchAllOptions() {
  return request({
    url: '/api/v1/config',
    method: 'get'
  })
}

export function updateOptions(options) {
  return request({
    url: '/api/v1/config',
    method: 'put',
    data: options
  })
}
