import request from '@/utils/request'

export function listAllPostComments (postId, pager, sort) {
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

export function replyToPost (postId, comment) {
  return request({
    url: `/api/v1/posts/${postId}/comments`,
    method: 'post',
    data: comment
  })
}

export function replyToComment (postId, parentCommentId, reply) {
  return request({
    url: `/api/v1/posts/${postId}/comments/${parentCommentId}/replies`,
    method: 'post',
    data: reply
  })
}

export function listChildComments (postId, commentId, sort) {
  return request({
    url: `/api/v1/posts/${postId}/comments/${commentId}/replies`,
    method: 'get',
    params: {
      sort: sort
    }
  })
}
