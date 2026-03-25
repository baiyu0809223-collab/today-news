<template>
  <div class="comment-section">
    <!-- 标题 -->
    <div class="comment-title">评论</div>

    <!-- 排序 -->
    <div class="sort-options">
      <el-radio-group v-model="sortType" size="small" @change="fetchComments">
        <el-radio-button label="time">最新</el-radio-button>
        <el-radio-button label="like">最热</el-radio-button>
      </el-radio-group>
    </div>

    <!-- 评论输入 -->
    <div class="comment-input">
      <el-avatar :src="currentUser.info.userPic || defaultAvatar" />
      <el-input
        v-model="newComment"
        type="textarea"
        :rows="3"
        maxlength="200"
        show-word-limit
        placeholder="说点什么吧～"
      />
      <el-button type="danger" @click="submitComment">发布</el-button>
    </div>

    <!-- 评论列表 -->
    <div class="comment-list">
      <div
        v-for="comment in comments.items"
        :key="comment.id"
        class="comment-card"
      >
        <!-- 评论头 -->
        <div class="comment-header">
          <el-avatar :src="comment.userPic || defaultAvatar" />
          <div class="user-meta">
            <span class="username">{{ comment.username }}</span>
            <span class="time">{{ formatTime(comment.create_time) }}</span>
          </div>
        </div>

        <!-- 内容 -->
        <div class="comment-content">{{ comment.content }}</div>

        <!-- 操作 -->
        <div class="comment-actions">
          <span class="like" @click="toggleLike(comment.id)">
            <span class="heart" :class="{ active: comment.isLiked }">❤</span>
            {{ comment.likeCount || 0 }}
          </span>

          <span class="reply" @click="showReplyInput(comment.id)">回复</span>

          <span
            v-if="comment.userId === currentUser.info.id || currentUser.info.role === 1"
            class="delete"
            @click="deleteComment(comment.id)"
          >
            删除
          </span>
        </div>

        <!-- 回复输入 -->
        <div v-if="activeReplyId === comment.id" class="reply-input">
          <el-input
            v-model="replyContent"
            type="textarea"
            :rows="2"
            placeholder="回复评论..."
          />
          <div class="reply-buttons">
            <el-button size="small" @click="cancelReply">取消</el-button>
            <el-button type="danger" size="small" @click="submitReply(comment.id)">
              回复
            </el-button>
          </div>
        </div>

        <!-- 回复列表 -->
        <div v-if="comment.replies?.length" class="reply-list">
          <div v-for="reply in comment.replies" :key="reply.id" class="reply-item">
            <el-avatar size="small" :src="reply.userPic || defaultAvatar" />
            <div class="reply-body">
              <div class="reply-user">
                {{ reply.username }}
                <span class="reply-time">{{ formatTime(reply.createTime) }}</span>
              </div>
              <div class="reply-text">{{ reply.content }}</div>
              <div class="reply-actions">
                <span class="like small" @click="toggleLike(reply.id)">
                  <span class="heart" :class="{ active: reply.isLiked }">❤</span>
                  {{ reply.likeCount || 0 }}
                </span>
                <span
                  v-if="reply.user_id === currentUser.info.id || currentUser.info.role === 1"
                  class="delete"
                  @click="deleteComment(reply.id)"
                >
                  删除
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <el-pagination
      background
      layout="prev, pager, next"
      :total="comments.total"
      :page-size="pageSize"
      :current-page="currentPage"
      @current-change="handlePageChange"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRoute } from 'vue-router'
import userInfoStore from '@/stores/userInfo'

import {
  CommentAddService,
  CommentListService,
  CommentDeleteService,
  CommentLikeService,
  CommentIsLikedService,
  CommentGetRepliesService
} from '@/api/article'

const route = useRoute()
const currentUser = userInfoStore()
const defaultAvatar = '/avatar.png'

const articleId = Number(route.params.id || route.query.id)

// 状态
const comments = ref({ total: 0, items: [] })
const newComment = ref('')
const replyContent = ref('')
const activeReplyId = ref(null)

const currentPage = ref(1)
const pageSize = ref(10)
const sortType = ref('time')

// 时间格式
const formatTime = (time) => {
  if (!time) return ''
  const d = new Date(time)
  return `${d.getMonth() + 1}-${d.getDate()} ${d.getHours()}:${String(d.getMinutes()).padStart(2, '0')}`
}

// 获取评论
const fetchComments = async () => {
  if (!articleId) return

  const res = await CommentListService(
    articleId,
    currentPage.value,
    pageSize.value,
    sortType.value
  )

  comments.value = res.data

  for (const c of comments.value.items) {
    c.isLiked = (await CommentIsLikedService(c.id)).data
    c.likeCount = c.likeCount || 0
    c.replies = (await CommentGetRepliesService(c.id)).data || []

    for (const r of c.replies) {
      r.isLiked = (await CommentIsLikedService(r.id)).data
      r.likeCount = r.likeCount || 0
    }
  }
}

// 发布评论
const submitComment = async () => {
  if (!newComment.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }

  await CommentAddService({
    articleId,
    content: newComment.value
  })

  newComment.value = ''
  fetchComments()
}

// 回复
const submitReply = async (parentId) => {
  if (!replyContent.value.trim()) return

  await CommentAddService({
    articleId,
    parentId,
    content: replyContent.value
  })

  cancelReply()
  fetchComments()
}

// 点赞
let likeLock = false
const toggleLike = async (id) => {
  if (likeLock) return
  likeLock = true

  await CommentLikeService(id)
  updateLike(id)

  setTimeout(() => (likeLock = false), 300)
}

const updateLike = (id) => {
  const find = (list) => list.find(i => i.id === id)

  const c = find(comments.value.items)
  if (c) {
    c.isLiked = !c.isLiked
    c.likeCount += c.isLiked ? 1 : -1
    return
  }

  comments.value.items.forEach(p => {
    const r = find(p.replies || [])
    if (r) {
      r.isLiked = !r.isLiked
      r.likeCount += r.isLiked ? 1 : -1
    }
  })
}

// 删除
const deleteComment = async (id) => {
  await CommentDeleteService(id)
  fetchComments()
}

// 回复控制
const showReplyInput = (id) => {
  activeReplyId.value = id
  replyContent.value = ''
}

const cancelReply = () => {
  activeReplyId.value = null
  replyContent.value = ''
}

// 分页
const handlePageChange = (p) => {
  currentPage.value = p
  fetchComments()
}

onMounted(fetchComments)
</script>

<style scoped>
.comment-section {
  padding: 16px;
  background: #fafafa;
}

.comment-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 12px;
}

.comment-input {
  display: flex;
  gap: 10px;
  margin-bottom: 16px;
}

.comment-card {
  background: #fff;
  border-radius: 14px;
  padding: 14px;
  margin-bottom: 14px;
  box-shadow: 0 4px 12px rgba(0,0,0,.04);
  animation: fadeUp .35s ease;
}

@keyframes fadeUp {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.comment-header {
  display: flex;
  gap: 10px;
}

.user-meta {
  display: flex;
  flex-direction: column;
}

.username {
  font-size: 14px;
  font-weight: 600;
}

.time {
  font-size: 12px;
  color: #aaa;
}

.comment-content {
  margin: 8px 0;
  font-size: 15px;
  line-height: 1.6;
}

.comment-actions {
  display: flex;
  gap: 18px;
  font-size: 13px;
}

.like {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
}

.heart {
  transition: all .25s;
}

.heart.active {
  color: #ff2c55;
  transform: scale(1.3);
}

.reply,
.delete {
  cursor: pointer;
  color: #999;
}

.reply:hover,
.delete:hover {
  color: #ff2c55;
}

.reply-list {
  margin-top: 10px;
  padding-left: 32px;
}

.reply-item {
  display: flex;
  gap: 8px;
  background: #f6f6f6;
  border-radius: 10px;
  padding: 8px;
  margin-bottom: 6px;
}

.reply-user {
  font-size: 13px;
  font-weight: 600;
}

.reply-time {
  font-size: 12px;
  color: #aaa;
  margin-left: 6px;
}
</style>