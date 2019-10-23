import Cookies from 'js-cookie'
const XsrfToken = 'I_AM_CSRF_TOKENS_FROM_MOCK_JS'
export default {
  initCsrfToken: res => {
    Cookies.set('XSRF-TOKEN', XsrfToken)
    Cookies.set('AUTH', 'false')
    return {
      code: 20000
    }
  }
}
