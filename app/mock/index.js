import Mock from 'mockjs'
import userAPI from './user'
import tableAPI from './table'
import registerAPI from './register'
import postAPI from './post'
import { param2Obj } from './utils'

// User
Mock.mock(/\/user\/info/, 'get', userAPI.getInfo)
Mock.mock(/\/user\/logout/, 'post', userAPI.logout)
Mock.mock(/\/user\/register/, 'post', userAPI.register)

Mock.mock(new RegExp('/api/v1/account/authentication'), 'post', userAPI.login)

Mock.mock(new RegExp('/api/v1/account'), 'get', userAPI.getInfo)

// Table
Mock.mock(/\/table\/list/, 'get', tableAPI.list)

Mock.mock(/\api\/blank/, 'get', registerAPI.initCsrfToken)

// Post
Mock.mock(new RegExp('/api/v1/posts/'), 'get', postAPI.listPosts)

