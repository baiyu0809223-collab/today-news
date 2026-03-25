<script setup>
import { ArticleGetDetailService } from '@/api/article'
import { userFindById } from '@/api/user'
import { ref, onMounted, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import Comment from '@/views/article/Comment.vue'

const route = useRoute()

/* ======================
   页面 & 数据
   ====================== */
const pageRef = ref(null)
const commentRef = ref(null)

const news = ref({
  title: '',
  content: '',
  create: '',
  updateTime: '',
  avatar: ''
})

/* ======================
   数据获取
   ====================== */
const GetDetail = async () => {
  const result = await ArticleGetDetailService(route.query.id)
  news.value = result.data

  const userId = news.value.createUser
  const create = await userFindById(userId)

  news.value.create = create.data.nickname
  news.value.avatar =
    create.data.userPic || 'https://dummyimage.com/80x80/eee/999'
}

/* ======================
   右侧按钮行为
   ====================== */

// ✅ 回到顶部
const scrollToTop = () => {
  pageRef.value?.scrollIntoView({
    behavior: 'smooth',
    block: 'start'
  })
}

// ✅ 滚动到评论区（仅点击时）
const scrollToComment = () => {
  commentRef.value?.scrollIntoView({
    behavior: 'smooth',
    block: 'start'
  })
}

// ✅ 分享
const sharePage = async () => {
  try {
    await navigator.clipboard.writeText(window.location.href)
    alert('链接已复制，可分享给好友 ✅')
  } catch (e) {
    alert('复制失败，请手动复制地址栏链接')
  }
}

// 占位
const likeArticle = () => {
  console.log('点赞逻辑待实现')
}

const collectArticle = () => {
  console.log('收藏逻辑待实现')
}

/* ======================
   生命周期
   ====================== */
onMounted(async () => {
  await GetDetail()

  // ✅ 强制进入页面显示顶部
  nextTick(() => {
    pageRef.value?.scrollIntoView({
      behavior: 'auto',
      block: 'start'
    })
  })
})
</script>

<template>
  <!-- ✅ 页面最外层 -->
  <div ref="pageRef" class="xiaohongshu-page">
    <!-- 文章卡片 -->
    <div class="note-card">
      <h1 class="note-title">{{ news.title }}</h1>

      <!-- 作者信息 -->
      <div class="author-info">
        <img :src="news.avatar" class="avatar" />
        <div class="author-meta">
          <span class="nickname">{{ news.create }}</span>
          <span class="time">{{ news.updateTime }}</span>
        </div>
      </div>

      <!-- 正文 -->
      <div class="note-content" v-html="news.content"></div>
    </div>

    <!-- 评论区（只有点击才滚） -->
    <div ref="commentRef" class="comment-wrapper">
      <Comment />
    </div>

    <!-- ✅ 右侧操作栏 -->
    <div class="right-action-bar">
      <div class="action-item" @click="scrollToTop">
        ⬆️
        <span>顶部</span>
      </div>

      <div class="action-item" @click="likeArticle">
        ❤️
        <span>点赞</span>
      </div>

      <div class="action-item" @click="collectArticle">
        ⭐
        <span>收藏</span>
      </div>

      <div class="action-item" @click="sharePage">
        🔗
        <span>分享</span>
      </div>

      <div class="action-item" @click="scrollToComment">
        💬
        <span>评论</span>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 页面背景 */
.xiaohongshu-page {
  background-color: #f7f7f7;
  min-height: 100vh;
  padding: 20px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* 主卡片 */
.note-card {
  width: 90%;
  max-width: 750px;
  background: #ffffff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.05);
}

/* 标题 */
.note-title {
  font-size: 26px;
  font-weight: 700;
  margin-bottom: 16px;
}

/* 作者信息 */
.author-info {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  margin-right: 12px;
}

.nickname {
  font-weight: 600;
}

.time {
  font-size: 12px;
  color: #999;
}

/* 正文 */
.note-content {
  line-height: 1.8;
}

/* 富文本图片限制 */
.note-content img {
  max-width: 100%;
  max-height: 80vh;
  height: auto;
  object-fit: contain;
  display: block;
  margin: 12px auto;
  border-radius: 12px;
}

/* 防止富文本撑破布局 */
.note-content * {
  max-width: 100%;
  box-sizing: border-box;
  word-break: break-word;
}

/* 评论区 */
.comment-wrapper {
  width: 90%;
  max-width: 750px;
  margin-top: 20px;
  background: #ffffff;
  border-radius: 16px;
  padding: 16px;
}

/* 右侧操作栏 */
.right-action-bar {
  position: fixed;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  z-index: 999;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.action-item {
  width: 56px;
  height: 56px;
  background: #ffffff;
  border-radius: 50%;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 20px;
  transition: transform 0.2s ease;
}

.action-item span {
  font-size: 12px;
  margin-top: 2px;
}

.action-item:hover {
  transform: scale(1.1);
  background: #f2f2f2;
}
</style>