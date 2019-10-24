import request from '@/utils/request'

export function listAllPostComments (postId, pager, sort) {
  console.log(pager)
  return request({
    url: `/api/v1/posts/${postId}/comments`,
    method: 'get',
    params: {
      page: pager.current > 0 ? pager.current - 1 : pager.current,
      size: pager.pageSize,
      sort: sort
    }
  })
}
