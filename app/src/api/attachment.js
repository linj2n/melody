import request from '@/utils/request'
import qs from 'qs'

export function fetchAttachments(query) {
  return request({
    url: '/api/v1/attachments',
    method: 'get',
    params: {
      title: query.title,
      size: query.limit,
      page: query.page > 0 ? query.page - 1 : 0,
      sort: query.sort
    },
    paramsSerializer: params => {
      return qs.stringify(params, { arrayFormat: 'repeat' })
    }
  })
}
