import request from '@/utils/request'

export function listPosts(query) {
  return request({
    url: '/api/v1/posts/',
    method: 'get',
    params: {
      size: query.limit,
      page: query.page > 0 ? query.page - 1 : 0,
      sort: query.sort
    }
  })
}
