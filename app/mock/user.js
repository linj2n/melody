import { param2Obj } from './utils'
import Cookies from 'js-cookie'
import qs from 'qs'

// const tokens = {
//   admin: {
//     token: 'admin-token'
//   },
//   editor: {
//     token: 'editor-token'
//   }
// }

const users = {
  'admin': {
    authorities: ['ROLE_ADMIN'],
    introduction: 'I am a super administrator',
    username: 'Super Admin'
  }
}

export default {
  register: res => {
    return {
      code: 20000,
      message: '激活邮件已发送'
    }
  },
  login: res => {
    // const { username } = JSON.parse(res.body)
    const data = users[qs.parse(res.body).username]
    console.log(data)
    Cookies.set('AUTH', 'true')
    if (data) {
      return {
        code: 20000,
        data
      }
    }
    return {
      code: 60204,
      message: 'Account and password are incorrect.'
    }
  },
  getInfo: res => {
    const info = users['admin']
    console.log(info)
    if (info) {
      return {
        code: 20000,
        data: info
      }
    }
    return {
      code: 50008,
      message: 'Login failed, unable to get user details.'
    }
  },
  logout: () => {
    return {
      code: 20000,
      data: 'success'
    }
  }
}
