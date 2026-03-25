<template>
  <el-container class="layout">
    <!-- ================= Aside ================= -->
    <el-aside
      class="aside xhs"
      :class="{ collapse: isCollapse }"
      :width="isCollapse ? '56px' : '208px'"
    >
      <!-- Logo -->
      <div class="logo-box">
        <img class="logo-img" src="@/assets/logo.png" />
        <span v-show="!isCollapse" class="logo-title">今日快讯</span>

        <el-icon class="collapse-btn" @click="toggleCollapse">
          <component :is="isCollapse ? Expand : Fold" />
        </el-icon>
      </div>

      <!-- Menu -->
      <el-menu
        router
        :default-active="route.path"
        :collapse="isCollapse"
        :collapse-transition="false"
        class="xhs-menu"
      >
        <el-menu-item index="/client/index">
          <el-icon><House /></el-icon>
          <template #title>首页</template>
        </el-menu-item>

        <el-sub-menu index="client">
          <template #title>
            <el-icon><Management /></el-icon>
            <span>你会喜欢</span>
          </template>
          <el-menu-item index="/category">分类浏览</el-menu-item>
          <el-menu-item index="/search">搜索</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="article">
          <template #title>
            <el-icon><UserFilled /></el-icon>
            <span>创作者空间</span>
          </template>
          <el-menu-item index="/article/manage">文章管理</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="me">
                  <template #title>
                    <el-icon><UserFilled /></el-icon>
                    <span>关于我的</span>
                  </template>
                  <el-menu-item
                    :index="`/client/userdetail?id=${userStore.info.id}`"
                  >
                    用户名片
                  </el-menu-item>     </el-sub-menu>

        <el-sub-menu index="manage">
          <template #title>
            <el-icon><UserFilled /></el-icon>
            <span>管理员中心</span>
          </template>
          <el-menu-item index="/manage/index">后台首页</el-menu-item>
          <el-menu-item index="/manage/category">文章分类</el-menu-item>
          <el-menu-item index="/manage/user">用户管理</el-menu-item>
          <el-menu-item index="/manage/examine">审核列表</el-menu-item>
        </el-sub-menu>

        <el-menu-item index="/test">
          <el-icon><Promotion /></el-icon>
          <template #title>测试</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- ================= Right ================= -->
    <el-container direction="vertical" style="height: 100%;">
      <!-- Header -->
      <el-header class="header">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item
            v-for="item in breadcrumbList"
            :key="item.path"
          >
            {{ item.meta.title }}
          </el-breadcrumb-item>
        </el-breadcrumb>

        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="avatar-box">
              <el-avatar :size="32" :src="userStore.info.userPic || avatar" />
              <el-icon><CaretBottom /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="info">基本资料</el-dropdown-item>
                <el-dropdown-item command="avatar">更换头像</el-dropdown-item>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- Tabs -->
      <div class="tabs-bar" v-if="!route.meta?.noTab">
        <el-tabs
          v-model="activeTab"
          type="card"
          @tab-remove="handleTabRemove"
        >
          <el-tab-pane
            v-for="tab in tabsStore.tabs"
            :key="tab.path"
            :label="' ' + tab.title"
            :name="tab.path"
            :closable="!tab.affix"
          />
        </el-tabs>
      </div>

      <!-- ✅ Main：自动撑满剩余空间 -->
      <el-main class="main" style="flex: 1;">
        <router-view />
      </el-main>

      <!-- ✅ Footer：页面最底部 + 水平居中 -->
      <el-footer
        class="footer"
        style="display: flex; justify-content: center; align-items: center;"
      >
        今日快讯 ©2024
      </el-footer>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  House,
  Management,
  UserFilled,
  Promotion,
  CaretBottom,
  Fold,
  Expand
} from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'

import avatar from '@/assets/default.png'
import userInfoStore from '@/stores/userInfo'
import { useTokenStore } from '@/stores/token'
import { useTabsStore } from '@/stores/tabs'
import{userInfoService} from '@/api/user'

const route = useRoute()
const router = useRouter()
const userStore = userInfoStore()
console.log(userStore)
const tokenStore = useTokenStore()
const tabsStore = useTabsStore()
const isCollapse = ref(false)

/* 面包屑 */
const breadcrumbList = computed(() =>
  route.matched.filter(r => r.meta?.title)
)

/* Tabs 当前激活 */
const activeTab = computed({
  get: () => route.path,
  set: path => router.push(path)
})

/* 路由变化加入 Tabs */
watch(
  () => route.path,
  () => {
    if (!route.meta?.noTab && route.meta?.title) {
      tabsStore.addTab(route)
    }
  },
  { immediate: true }
)

/* 删除 Tab */
const handleTabRemove = (path) => {
  const index = tabsStore.tabs.findIndex(t => t.path === path)
  tabsStore.removeTab(path)

  if (route.path === path) {
    const next = tabsStore.tabs[index - 1] || tabsStore.tabs[0]
    next && router.push(next.path)
  }
}

/* 折叠菜单 */
const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

/* 用户菜单 */
const handleCommand = (command) => {
  if (command === 'logout') {
    ElMessageBox.confirm('确认退出登录？', '提示', { type: 'warning' })
      .then(() => {
        tokenStore.removeToken()
        userStore.removeInfo()
        router.push('/login')
        ElMessage.success('退出成功')
      })
    return
  }
  router.push('/user/' + command)
}
</script>

<style scoped lang="scss">
/* ================= Layout ================= */
.layout {
  height: 100vh;
  background: #f8f8f8;
}

/* ================= Aside ================= */
.aside.xhs {
  background: #fff;
  border-right: 1px solid #f0f0f0;
  transition: width .3s cubic-bezier(.4,0,.2,1);
}

/* Logo */
.logo-box {
  height: 64px;
  padding: 0 16px;
  display: flex;
  align-items: center;
  color: #ff2442;
}

.logo-img {
  width: 32px;
  height: 32px;
}

.logo-title {
  margin-left: 12px;
  font-size: 16px;
  font-weight: 600;
}

.collapse-btn {
  margin-left: auto;
  cursor: pointer;
}

/* ================= Menu ================= */
.xhs-menu {
  border-right: none;
}

/* 正常状态 */
:deep(.el-menu-item),
:deep(.el-sub-menu__title) {
  height: 44px;
  margin: 6px 12px;
  border-radius: 12px;
  color: #333;
  transition: background .25s, color .25s;
}

/* hover */
:deep(.el-menu-item:hover),
:deep(.el-sub-menu__title:hover) {
  background: #fff1f3;
  color: #ff2442;
}

/* active */
:deep(.el-menu-item.is-active) {
  background: #ffe4e8;
  color: #ff2442;
  font-weight: 600;
}

/* collapse 状态下禁用装饰 */
.aside.collapse {
  :deep(.el-menu-item),
  :deep(.el-sub-menu__title) {
    margin: 0;
    border-radius: 0;
    background: transparent !important;
  }
}

/* ================= Header / Tabs / Main ================= */
.header {
  height: 56px;
  background: #fff;
  padding: 0 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #f2f2f2;
}

.header-right {
  display: flex;
  align-items: center;
}

.tabs-bar {
  background: #fff;
  padding: 10px 16px;
  border-bottom: 1px solid #f5f5f5;
}

:deep(.el-tabs__item) {
  height: 28px;
  line-height: 28px;
  font-size: 12px;
  border-radius: 999px;
  margin-right: 8px;
  border: 1px solid #ffe4e8;
  color: #ff2442;
}

:deep(.el-tabs__item.is-active) {
  background: #ff2442;
  color: #fff;
}

.main {
  padding: 16px;
}

.footer {
  text-align: center;
  font-size: 12px;
  color: #aaa;
}
</style>