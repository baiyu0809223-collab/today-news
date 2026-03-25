<template>
  <div class="user-detail-page">
    <!-- 顶部背景 -->
    <div class="header-bg"></div>

    <!-- 用户被禁用 -->
    <div v-if="userDetail.status === 0" class="disabled-tip">
      该用户已被禁用
    </div>

    <!-- 名片 -->
    <div v-else class="card">
      <!-- 头像 -->
      <img
        class="avatar"
        :src="userDetail.avatar || defaultAvatar"
      />

      <!-- 昵称 + 认证 -->
      <div class="name-line">
        <h2>{{ userDetail.nickname || '未设置昵称' }}</h2>
        <span
          v-if="userDetail.isVerified === 1"
          class="verified"
        >✔</span>
      </div>

      <!-- 用户名 -->
      <div class="username">
        @{{ userDetail.username || userDetail.userId }}
      </div>

      <!-- 简介 -->
      <p class="bio">
        {{ userDetail.bio || '这个人很神秘，什么都没有留下～' }}
      </p>

      <!-- 数据统计 -->
      <div class="count-bar">
        <div class="count-item">
          <span class="count">{{ userDetail.followCount || 0 }}</span>
          <span class="label">关注</span>
        </div>
        <div class="count-item">
          <span class="count">{{ userDetail.fanCount || 0 }}</span>
          <span class="label">粉丝</span>
        </div>
        <div class="count-item">
          <span class="count">{{ userDetail.likeCount || 0 }}</span>
          <span class="label">获赞</span>
        </div>
      </div>

      <!-- 标签 -->
      <div class="tags" v-if="tagList.length">
        <span
          class="tag"
          v-for="(tag, index) in tagList"
          :key="index"
        >
          # {{ tag }}
        </span>
      </div>

      <!-- 个人信息 -->
      <div class="info">
        <div v-if="userDetail.gender !== null">
          性别：{{ genderText }}
        </div>
        <div v-if="userDetail.birthday">
          生日：{{ userDetail.birthday }}
        </div>
        <div v-if="userDetail.location">
          地区：{{ userDetail.location }}
        </div>
        <div v-if="userDetail.website">
          主页：
          <a
            :href="userDetail.website"
            target="_blank"
          >
            {{ userDetail.website }}
          </a>
        </div>
        <div v-if="userDetail.lastActiveTime">
          最近活跃：{{ userDetail.lastActiveTime }}
        </div>
      </div>

      <!-- 操作 -->
      <div class="actions">
        <button class="follow-btn">关注</button>
        <button class="message-btn">私信</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getUserDetailByUserIdService } from '@/api/user'

const route = useRoute()
const userId = route.query.id

const defaultAvatar =
  'https://cdn.jsdelivr.net/gh/xaoxuu/assets@master/avatar/avatar.png'

const userDetail = ref({})

/* 标签处理 */
const tagList = computed(() => {
  if (!userDetail.value.tags) return []
  return userDetail.value.tags.split(',').filter(Boolean)
})

/* 性别文字 */
const genderText = computed(() => {
  const map = {
    0: '未知',
    1: '男',
    2: '女'
  }
  return map[userDetail.value.gender]
})

onMounted(async () => {
  if (!userId) return
  const res = await getUserDetailByUserIdService(userId)
  userDetail.value = res.data
})
</script>

<style scoped>
.user-detail-page {
  min-height: 100vh;
  background: #f8f8f8;
}

/* 顶部背景 */
.header-bg {
  height: 220px;
  background: linear-gradient(135deg, #ff4d4f, #ff7a45);
}

/* 禁用提示 */
.disabled-tip {
  margin: 40px auto;
  text-align: center;
  color: #999;
}

/* 名片 */
.card {
  width: 92%;
  max-width: 420px;
  margin: -120px auto 0;
  background: #fff;
  border-radius: 18px;
  padding: 24px;
  text-align: center;
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.1);
}

/* 头像 */
.avatar {
  width: 96px;
  height: 96px;
  border-radius: 50%;
  margin-top: -72px;
  object-fit: cover;
  border: 4px solid #fff;
}

/* 昵称 */
.name-line {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
}

.name-line h2 {
  font-size: 20px;
}

.verified {
  color: #1890ff;
  font-size: 16px;
}

/* 用户名 */
.username {
  font-size: 13px;
  color: #999;
  margin-bottom: 10px;
}

/* 简介 */
.bio {
  font-size: 14px;
  color: #555;
  margin: 12px 0;
}

/* 统计栏 */
.count-bar {
  display: flex;
  justify-content: space-around;
  margin: 18px 0;
}

.count-item {
  text-align: center;
}

.count {
  font-size: 18px;
  font-weight: bold;
}

.label {
  font-size: 12px;
  color: #888;
}

/* 标签 */
.tags {
  margin-bottom: 16px;
}

.tag {
  display: inline-block;
  margin: 4px;
  padding: 4px 10px;
  background: #fff1f0;
  color: #ff4d4f;
  border-radius: 12px;
  font-size: 12px;
}

/* 信息 */
.info {
  text-align: left;
  font-size: 13px;
  color: #666;
  margin-bottom: 18px;
}

.info div {
  margin: 4px 0;
}

.info a {
  color: #1890ff;
}

/* 操作按钮 */
.actions {
  display: flex;
  justify-content: center;
  gap: 12px;
}

.follow-btn {
  background: #ff4d4f;
  color: #fff;
  border: none;
  padding: 8px 26px;
  border-radius: 20px;
}

.message-btn {
  background: #f2f2f2;
  color: #333;
  border: none;
  padding: 8px 26px;
  border-radius: 20px;
}
</style>