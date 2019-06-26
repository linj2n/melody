import request from '@/utils/request'
import qs from 'qs'

export function fetchAllOptions() {
  return request({
    url: '/api/v1/config',
    method: 'get'
  })
}

export function fetchOptions(names) {
  return request({
    url: '/api/v1/config',
    method: 'get',
    params: {
      optionName: names
    },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'repeat' })
    }
  })
}

export function updateOptions(options) {
  return request({
    url: '/api/v1/config',
    method: 'put',
    data: options
  })
}
