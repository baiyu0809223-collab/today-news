<script setup>
import { ref, onMounted, computed } from 'vue'
import { ArticleListService } from '@/api/article'

/* ================= 查询参数 ================= */
const queryForm = ref({
  page: 1,
  size: 10,
  title: '',
  state: '已发布'
})

/* ================= 数据 ================= */
const articles = ref([])
const total = ref(0)
const loading = ref(false)

/* ================= 查询文章 ================= */
const loadArticles = async () => {
  loading.value = true
  try {
    const res = await ArticleListService(queryForm.value)
    articles.value = res.data.items
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

/* ================= 搜索 ================= */
const onSearch = () => {
  queryForm.value.page = 1
  loadArticles()
}

/* ================= 分页 ================= */
const handlePageChange = (page) => {
  queryForm.value.page = page
  loadArticles()
}

/* ================= 工具方法 ================= */

/** 去除 HTML 标签 */
const stripHtml = (html = '') => {
  return html.replace(/<[^>]+>/g, '')
}

/** 关键字高亮 */
const highlight = (text = '') => {
  if (!queryForm.value.title) return text
  const keyword = queryForm.value.title
  const reg = new RegExp(`(${keyword})`, 'gi')
  return text.replace(reg, '<span class="highlight">$1</span>')
}

/* ================= 初始化 ================= */
onMounted(() => {
  loadArticles()
})
</script>

<template>
  <el-container>
    <el-main class="page">

      <!-- 🔍 搜索 -->
      <div class="search-bar">
        <el-input
          v-model="queryForm.title"
          placeholder="搜索你感兴趣的内容"
          clearable
          @keyup.enter="onSearch"
        />
        <el-button type="danger" @click="onSearch">
          搜索
        </el-button>
      </div>

      <!-- 📄 内容流 -->
      <div v-loading="loading" class="card-list">
        <div
          v-for="item in articles"
          :key="item.id"
          class="note-card"
        >
          <img
            :src="item.coverImg"
            class="cover"
          />

          <div class="content-box">
            <h3
              class="title"
              v-html="highlight(item.title)"
            />

            <p
              class="content"
              v-html="highlight(stripHtml(item.content))"
            />

            <div class="meta">
              <span>{{ item.createUsername }}</span>
              <span>{{ item.updateTime }}</span>
              <span>👁 {{ item.views }}</span>
            </div>
          </div>
        </div>

        <!-- 📄 分页 -->
        <el-pagination
          v-if="total > 0"
          class="pagination"
          background
          layout="prev, pager, next"
          :current-page="queryForm.page"
          :page-size="queryForm.size"
          :total="total"
          @current-change="handlePageChange"
        />
      </div>

    </el-main>
  </el-container>
</template>

<style scoped>
/* 页面整体 */
.page {
  background: #f7f7f7;
  min-height: 100vh;
}

/* 搜索栏 */
.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

/* 卡片流 */
.card-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
}

/* 小红书卡片 */
.note-card {
  background: #fff;
  border-radius: 14px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.note-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

/* 封面图 */
.cover {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

/* 内容 */
.content-box {
  padding: 14px;
}

.title {
  font-size: 17px;
  font-weight: 600;
  line-height: 1.4;
  margin-bottom: 8px;
  color: #222;
}

.content {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 10px;

  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

/* 元信息 */
.meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #999;
}

/* 高亮关键字 */
.highlight {
  color: #ff2442;
  font-weight: 600;
}

/* 分页 */
.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}
</style>