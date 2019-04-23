import request from '@/utils/request'

export function listPosts(query) {
  return request({
    url: '/api/v1/posts/',
    method: 'get',
    params: query
  })
}
