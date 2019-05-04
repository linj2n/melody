import request from '@/utils/request'
import qs from 'qs'

export function listPosts(query) {
  return request({
    url: '/api/v1/posts/search',
    method: 'get',
    params: {
      title: query.title,
      tagId: query.tagId,
      categoryId: query.categoryId,
      size: query.limit,
      page: query.page > 0 ? query.page - 1 : 0,
      sort: query.sort
    },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'repeat' })
    }
  })
}

export function listAllCategories() {
  return request({
    url: '/api/v1/categories',
    method: 'get'
  })
}

export function listAllTags() {
  return request({
    url: '/api/v1/tags',
    method: 'get'
  })
}

export function requestToNewPost() {
  return request({
    url: '/api/v1/posts/new',
    method: 'post'
  })
}

export function fecthPost(id) {
  return request({
    url: '/api/v1/posts/' + id,
    method: 'get'
  })
}

export function updatePost(post) {
  console.log(post)
  return request({
    url: '/api/v1/posts/' + post.id,
    method: 'put',
    data: post
    // data: Object.assign({}, post)
    // data: {
    //   id: post.id,
    //   title: post.title,
    //   summary: post.summary,
    //   content: post.content,
    //   status: post.status,
    //   url: post.url,
    //   views: post.views,
    //   createdAt: post.createdAt,
    //   updatedAt: post.updatedAt
    // }
  })
}
