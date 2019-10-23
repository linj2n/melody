import Mock from 'mockjs'

const List = []
const count = 100
const tagCount = 20
const categoryCount = 20

const tags = []
const categories = []
for (let i = 0; i < tagCount; i++) {
  tags.push(Mock.mock({
    id: '@increment',
    name: '@cword(2,6)'
  }))
  // tags.push(Mock.mock('@cword(2, 6)'))
}

for (let i = 0; i < categoryCount; i++) {
  categories.push(Mock.mock({
    id: '@increment',
    name: '@cword(3,8)'
  }))
  // categories.push(Mock.mock('@cword(3,7'))
}

const baseContent = '<p>I am testing data, I am testing data.</p><p><img src="https://wpimg.wallstcn.com/4c69009c-0fd4-4153-b112-6cb53d1cf943"></p>'
// const image_uri = 'https://wpimg.wallstcn.com/e4558086-631c-425c-9430-56ffb46e70b3'

for (let i = 0; i < count; i++) {
  List.push(Mock.mock({
    id: '@increment',
    // 创建时间
    createdAt: '@datetime("yyyy-MM-dd HH:mm:ss")',
    // 浏览次数
    views: '@first',
    // 标题
    title: '@title(5, 10)',
    // 概述
    summary: 'mock data',
    // 内容
    content: baseContent,
    // display_time: '@datetime',
    // comment_disabled: true,
    // pageviews: '@integer(300, 5000)',
    // image_uri,
    // platforms: ['a-platform']
    'status|1': ['PUBLISHED', 'DRAFT'],
    'tags|0-20': tags,
    'categories|0-20': categories
  }))
}
export default {
  listPosts: config => {
    // const { importance, type, title, page = 1, limit = 20, sort } = config.query

    // let mockList = List.filter(item => {
    //   if (importance && item.importance !== +importance) return false
    //   if (type && item.type !== type) return false
    //   if (title && item.title.indexOf(title) < 0) return false
    //   return true
    // })

    // if (sort === '-id') {
    //   mockList = mockList.reverse()
    // }
    const { page = 1, limit = 20 } = config

    const pageList = List.filter((item, index) => index < limit * page && index >= limit * (page - 1))

    return {
      code: 20000,
      data: {
        total: List.length,
        content: pageList
      }
    }
  }
}

// export default [
//   {
//     url: '/article/list',
//     type: 'get',
//     response: config => {
//       // const { importance, type, title, page = 1, limit = 20, sort } = config.query

//       // let mockList = List.filter(item => {
//       //   if (importance && item.importance !== +importance) return false
//       //   if (type && item.type !== type) return false
//       //   if (title && item.title.indexOf(title) < 0) return false
//       //   return true
//       // })

//       // if (sort === '-id') {
//       //   mockList = mockList.reverse()
//       // }
//       const { page = 1, limit = 20 } = config.query

//       const pageList = List.filter((item, index) => index < limit * page && index >= limit * (page - 1))

//       return {
//         code: 20000,
//         data: {
//           total: List.length,
//           items: pageList
//         }
//       }
//     }
//   },

//   {
//     url: '/article/detail',
//     type: 'get',
//     response: config => {
//       const { id } = config.query
//       for (const article of List) {
//         if (article.id === +id) {
//           return {
//             code: 20000,
//             data: article
//           }
//         }
//       }
//     }
//   },

//   {
//     url: '/article/pv',
//     type: 'get',
//     response: _ => {
//       return {
//         code: 20000,
//         data: {
//           pvData: [
//             { key: 'PC', pv: 1024 },
//             { key: 'mobile', pv: 1024 },
//             { key: 'ios', pv: 1024 },
//             { key: 'android', pv: 1024 }
//           ]
//         }
//       }
//     }
//   },

//   {
//     url: '/article/create',
//     type: 'post',
//     response: _ => {
//       return {
//         code: 20000,
//         data: 'success'
//       }
//     }
//   },

//   {
//     url: '/article/update',
//     type: 'post',
//     response: _ => {
//       return {
//         code: 20000,
//         data: 'success'
//       }
//     }
//   }
// ]

