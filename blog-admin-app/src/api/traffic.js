import request from '@/utils/request'

export function fetchSiteTrafficData() {
  return request({
    url: '/api/v1/site/traffic-data',
    method: 'get'
  })
}

export function fetchCommentCountData() {
  return request({
    url: '/api/v1/comments/recent-count',
    method: 'get'
  })
}

export function fetchSiteTotalViews() {
  return request({
    url: '/api/v1/site/views/total-count',
    method: 'get'
  })
}

export function fetchPostTotalNumber() {
  return request({
    url: '/api/v1/posts/total-count',
    method: 'get'
  })
}

export function fetchCommentTotalNumber() {
  return request({
    url: '/api/v1/comments/total-count',
    method: 'get'
  })
}
