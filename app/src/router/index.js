import Vue from 'vue'
import Router from 'vue-router'

// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    title: 'title'               the name show in subMenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    breadcrumb: false            if false, the item will hidden in breadcrumb(default is true)
  }
**/
export const constantRouterMap = [
  { path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { path: '/register', component: () => import('@/views/register/index'), hidden: true },
  { path: '/404', component: () => import('@/views/404'), hidden: true },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: 'Dashboard',
    hidden: true,
    children: [{
      path: 'dashboard',
      component: () => import('@/views/dashboard/index')
    }]
  }
]

export const asyncRouterMap = [
  {
    path: '/posts',
    component: Layout,
    meta: { roles: ['ROLE_ADMIN'] },
    children: [
      {
        path: '',
        name: 'posts',
        component: () => import('@/views/posts/list'),
        meta: { title: '文 章', icon: 'documentation', roles: ['ROLE_ADMIN'] }
      },
      {
        path: ':id/edit',
        name: 'post-edit',
        component: () => import('@/views/posts/edit'),
        hidden: true,
        meta: { title: '编辑文章', icon: 'icon_doc', roles: ['ROLE_ADMIN'] }
      }
    ]
  },
  // TODO: Tags component
  {
    path: '/tags',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Tags',
        component: () => import('@/views/category/index'),
        meta: { title: '标 签', icon: 'tree-table' }
      }
    ]
  },

  // TODO: Categories component
  {
    path: '/attachments',
    component: Layout,
    children: [
      {
        path: '',
        name: 'Attachments',
        component: () => import('@/views/attachments/index'),
        meta: { title: '附 件', icon: 'clipboard' }
      }
    ]
  },

  // TODO: Setting component
  {
    path: '/setting',
    component: Layout,
    children: [
      {
        path: '',
        name: 'Setting',
        component: () => import('@/views/setting/index'),
        meta: { title: '配 置', icon: 'user' }
      }
    ]
  },
  { path: '*', redirect: '/404', hidden: true }
]

export default new Router({
  mode: 'history', // 后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})
