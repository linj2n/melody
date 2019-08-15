import request from '@/utils/request'

export function fetchSiteUniqueVisitorData() {
  return request({
    url: '/api/v1/site/unique_visitors',
    method: 'get'
  })
}

export function fetchSitePageViewData() {
  return request({
    url: '/api/v1/site/views',
    method: 'get'
  })
}
