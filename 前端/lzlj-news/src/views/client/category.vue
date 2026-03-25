<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

import {
  ArticleGetAllService,
  ArticleCategoryAllListService
} from '@/api/article.js'

/* ===== 路由 ===== */
const route = useRoute()
const router = useRouter()

/* ===== 分类 ===== */
const categories = ref([])
const currentCategoryId = ref(route.query.id || null)
const currentCategoryName = ref('')

/* ===== 文章 ===== */
const articles = ref([])
const loading = ref(false)

/* ===== 加载分类 ===== */
const loadCategories = async () => {
  try {
    const res = await ArticleCategoryAllListService()
    categories.value = res.data || []

    if (!currentCategoryId.value && categories.value.length) {
      selectCategory(categories.value[0])
    } else {
      const target = categories.value.find(
        c => c.id == currentCategoryId.value
      )
      target && (currentCategoryName.value = target.categoryName)
    }
  } catch {
    ElMessage.error('获取分类失败')
  }
}

/* ===== 获取文章 ===== */
const loadArticles = async () => {
  try {
    loading.value = true
    const res = await ArticleGetAllService({
      categoryId: currentCategoryId.value
    })
    articles.value = res.data || []
  } catch {
    ElMessage.error('获取文章失败')
  } finally {
    loading.value = false
  }
}

/* ===== 选择分类 ===== */
const selectCategory = (category) => {
  currentCategoryId.value = category.id
  currentCategoryName.value = category.categoryName

  router.replace({
    path: '/category',
    query: { id: category.id }
  })

  loadArticles()
}

/* ===== 监听 URL ===== */
watch(
  () => route.query.id,
  (id) => {
    currentCategoryId.value = id
    const target = categories.value.find(c => c.id == id)
    if (target) {
      currentCategoryName.value = target.categoryName
      loadArticles()
    }
  }
)

onMounted(async () => {
  await loadCategories()
  if (currentCategoryId.value) loadArticles()
})
</script>

<template>
  <div class="xhs-page">
    <!-- 头部 -->
    <div class="xhs-header">
      <h2>{{ currentCategoryName || '发现精彩内容' }}</h2>
      <p>记录真实生活 · 分享美好瞬间</p>
    </div>

    <el-row :gutter="20">
      <!-- 左侧：兴趣标签 -->
      <el-col :span="5">
        <div class="tag-panel">
          <div
            v-for="item in categories"
            :key="item.id"
            class="tag-item"
            :class="{ active: item.id === currentCategoryId }"
            @click="selectCategory(item)"
          >
            # {{ item.categoryName }}
          </div>
        </div>
      </el-col>

      <!-- 右侧：内容流 -->
      <el-col :span="19">
        <el-skeleton v-if="loading" :rows="6" animated />

        <el-empty
          v-else-if="articles.length === 0"
          description="还没有人分享哦～"
        />

        <div v-else class="xhs-list">
          <div
            v-for="item in articles"
            :key="item.id"
            class="xhs-card"
            @click="$router.push({ path: '/detail', query: { id: item.id } })"
          >
            <div class="cover">
              <img :src="item.coverImg" />
            </div>

            <div class="content">
              <h3 class="title">{{ item.title }}</h3>
              <p class="desc" v-html="item.content"></p>

              <div class="meta">
                <span class="time">{{ item.updateTime }}</span>
                <span class="like">❤️ 喜欢</span>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped lang="scss">
/* ================= 整体 ================= */
.xhs-page {
  padding: 20px;
  background: #f6f6f6;
}

/* ================= 头部 ================= */
.xhs-header {
  margin-bottom: 24px;

  h2 {
    margin: 0;
    font-size: 26px;
    font-weight: 700;
  }

  p {
    margin-top: 6px;
    font-size: 13px;
    color: #888;
  }
}

/* ================= 标签 ================= */
.tag-panel {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.tag-item {
  padding: 10px 14px;
  border-radius: 999px;
  font-size: 14px;
  background: #fff;
  color: #555;
  cursor: pointer;
  transition: all 0.25s;
}

.tag-item:hover {
  background: #ffecec;
  color: #ff4d4f;
}

.tag-item.active {
  background: linear-gradient(90deg, #ff4d4f, #ff7875);
  color: #fff;
  box-shadow: 0 6px 14px rgba(255, 77, 79, 0.3);
}

/* ================= 内容流 ================= */
.xhs-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

/* 单卡片 */
.xhs-card {
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
}

.xhs-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.08);
}

/* 封面 */
.cover img {
  width: 100%;
  height: 220px;
  object-fit: cover;
}

/* 内容 */
.content {
  padding: 14px;
}

.title {
  margin: 0 0 8px;
  font-size: 16px;
  font-weight: 600;
  line-height: 1.4;
}

.desc {
  font-size: 14px;
  color: #666;
  line-height: 1.6;

  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

/* 底部 */
.meta {
  margin-top: 12px;
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #999;
}

.like {
  color: #ff4d4f;
}
</style>