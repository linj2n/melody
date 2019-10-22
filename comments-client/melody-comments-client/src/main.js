import Vue from 'vue'
// import Button from 'ant-design-vue/lib/button'
// import 'ant-design-vue/dist/antd.css'
import { Button, Tooltip, Avatar, Icon, Comment, List, Input, Form, Row, Col, Divider, Spin } from 'ant-design-vue'
import App from './App'
// import Antd from 'ant-design-vue'

// Vue.component(Button.name, Button, Avatar, Icon, Tooltip, Tooltip.name, Comment, Comment.name)
Vue.use(Button)
Vue.use(Comment)
Vue.use(Tooltip)
Vue.use(Avatar)
Vue.use(Icon)
Vue.use(List)
Vue.use(Input)
Vue.use(Form)
Vue.use(Row)
Vue.use(Col)
Vue.use(Divider)
Vue.use(Spin)

Vue.config.productionTip = false

new Vue({
  render: h => h(App)
}).$mount('#app')
