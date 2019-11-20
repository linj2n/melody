import request from '@/utils/request'
import { getAuthState } from '@/utils/auth'

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
  const URL = isAnonymousAuthor()
    ? `/api/v1/posts/${postId}/anonymous-comments`
    : `/api/v1/posts/${postId}/comments`
  return request({
    url: URL,
    method: 'post',
    data: comment
  })
}

export function replyToComment (postId, parentCommentId, reply) {
  const URL = isAnonymousAuthor()
    ? `/api/v1/posts/${postId}/comments/${parentCommentId}/anonymous-replies`
    : `/api/v1/posts/${postId}/comments/${parentCommentId}/replies`
  return request({
    url: URL,
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

function isAnonymousAuthor () {
  return !getAuthState() || getAuthState() === 'false'
}
