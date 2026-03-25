import { createRouter, createWebHistory } from 'vue-router'

// ===== 基础页面 =====
import LoginVue from '@/views/Login.vue'
import LayoutVue from '@/views/Layout.vue'

// ===== 后台管理 =====
import ArticleCategoryVue from '@/views/Manage/ArticleCategory.vue'
import ArticleManageVue from '@/views/article/ArticleManage.vue'
import ManageIndex from '@/views/Manage/index.vue'
import ManageUserVue from '@/views/Manage/manage_UserManage.vue'
import Examine from '@/views/Manage/Examine.vue'

// ===== 用户中心 =====
import UserAvatarVue from '@/views/user/UserAvatar.vue'
import UserInfoVue from '@/views/user/UserInfo.vue'
import UserResetPasswordVue from '@/views/user/UserResetPassword.vue'

// ===== 前台 =====
import AppStart from '@/views/client/index.vue'
import Detail from '@/views/client/detail.vue'
import Category from '@/views/client/category.vue'
import Search from '@/views/client/search.vue'
import Me from '@/views/client/userdetail.vue'

// ===== 其他 =====
import Test from '@/views/test.vue'

// ================= 路由定义 =================
const routes = [
  /* 登录页（无 Layout） */
  {
    path: '/login',
    component: LoginVue,
    meta: { title: '登录' }
  },

  /* 主框架 */
  {
    path: '/',
    component: LayoutVue,
    redirect: '/client/index',
    children: [
      /* ===== 前台 ===== */
      {
        path: '/client/index',
        component: AppStart,
        meta: {
          title: '首页',
          affix: true // ✅ Tabs 固定
        }
      },
      {
        path: '/detail',
        component: Detail,
        meta: {
          title: '文章详情',
          noTab: true // ✅ 文章详情页不显示 Tabs
        }
      },
      {
        path: '/category',
        component: Category,
        meta: { title: '分类浏览' }
      },
      {
        path: '/search',
        component: Search,
        meta: { title: '搜索' }
      },

      /* ===== 创作者中心 ===== */
      {
        path: '/article/manage',
        component: ArticleManageVue,
        meta: { title: '文章管理' }
      },
      {
              path: '/client/userdetail',
              component: Me,
              meta: { title: '关于我的',noTab: true }
            },

      /* ===== 管理员中心 ===== */
      {
        path: '/manage/index',
        component: ManageIndex,
        meta: { title: '后台首页' }
      },
      {
        path: '/manage/category',
        component: ArticleCategoryVue,
        meta: { title: '文章分类' }
      },
      {
        path: '/manage/user',
        component: ManageUserVue,
        meta: { title: '用户管理' }
      },
      {
        path: '/manage/examine',
        component: Examine,
        meta: { title: '审核列表' }
      },

      /* ===== 用户中心 ===== */
      {
        path: '/user/info',
        component: UserInfoVue,
        meta: { title: '基本资料' }
      },
      {
        path: '/user/avatar',
        component: UserAvatarVue,
        meta: { title: '更换头像' }
      },
      {
        path: '/user/resestpassword',
        component: UserResetPasswordVue,
        meta: { title: '修改密码' }
      },

      /* ===== 测试 ===== */
      {
        path: '/test',
        component: Test,
        meta: { title: '测试页面' }
      }
    ]
  }
]

// ================= 创建路由器 =================
const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router