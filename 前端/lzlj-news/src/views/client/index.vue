<template>
  <div class="home">

    <!--  今日聚焦 -->
    <section class="ai-focus fade-in">
      <div class="ai-header">
        <div class="ai-orb" :class="{ scanning: aiLoading }"></div>
        <h2>今日新闻快讯</h2>

        <button
          class="voice-btn"
          @click="toggleVoice"
          :disabled="!aiSummary"
          title="语音播报"
        >
          {{ speaking ? '🔇' : '🔊' }}
        </button>
      </div>

      <p v-if="aiLoading" class="ai-loading">
        正在扫描全网热点…
      </p>

      <p v-else class="ai-text">
        {{ displayedSummary }}
        <span class="cursor">▍</span>
      </p>

      <div class="ai-tags" v-if="aiTags.length">
        <span
          class="ai-tag"
          v-for="tag in aiTags"
          :key="tag"
        >
          #{{ tag }}
        </span>
      </div>

      <button class="main-btn" @click="generateAiSummary">
        换一条看看
      </button>
    </section>

    <!-- 🗞 主体区域 -->
    <div class="main">

      <!-- 最新文章 -->
      <section class="article-list">
        <h2 class="section-title">最新文章</h2>

        <div v-if="loading" class="loading">
          加载中…
        </div>

        <div v-else class="list">
          <article
            class="article-item reveal"
            v-for="item in articles"
            :key="item.id"
          >
            <div class="cover-wrapper">
              <img :src="item.coverImg" class="cover" />
            </div>

            <div class="content">
              <h3 class="title">
                <router-link
                  :to="'/detail?id=' + item.id"
                  class="title-link"
                >
                  {{ item.title }}
                </router-link>
              </h3>

              <p class="meta">
                {{ authorMap[item.createUser] || `用户${item.createUser}` }}
                · {{ formatTime(item.createTime) }}
                · 👀 {{ item.views }}
              </p>

              <div class="actions">
                <span
                  class="action"
                  :class="{ active: likes[item.id] }"
                  @click="toggleLike(item.id)"
                >
                  ❤️ {{ likes[item.id] ? '已赞' : '点赞' }}
                </span>

                <span
                  class="action"
                  :class="{ active: favorites[item.id] }"
                  @click="toggleFavorite(item.id)"
                >
                  ⭐ {{ favorites[item.id] ? '已收藏' : '收藏' }}
                </span>
              </div>
            </div>
          </article>
        </div>
      </section>

      <!-- 🔥 热搜榜 -->
      <aside class="hot-rank">
        <h3 class="hot-title">🔥 热搜榜</h3>

        <ul class="hot-list">
          <li
            v-for="(item, index) in hotArticles"
            :key="item.id"
            class="hot-item"
          >
            <span class="rank" :class="'r' + index">
              {{ index + 1 }}
            </span>

            <router-link
              :to="'/detail?id=' + item.id"
              class="hot-link"
            >
              {{ item.title }}
            </router-link>

            <span class="hot-views">
              {{ item.views }}
            </span>
          </li>
        </ul>
      </aside>

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed, nextTick } from 'vue'
import request from '@/util/request'
import { ArticleGetAllService } from '@/api/article.js'
import { userFindById } from '@/api/user.js'

/* ========================
   状态
======================== */

const articles = ref([])
const loading = ref(false)
const authorMap = ref({})

const aiSummary = ref('')
const displayedSummary = ref('')
const aiLoading = ref(false)
const aiTags = ref([])

const likes = ref({})
const favorites = ref({})

let typingTimer = null
let utterance = null
const speaking = ref(false)

/* ========================
   热搜榜
======================== */

const hotArticles = computed(() => {
  return [...articles.value]
    .sort((a, b) => b.views - a.views)
    .slice(0, 8)
})

/* ========================
   AI 打字机
======================== */

const typeSummary = (text) => {
  clearInterval(typingTimer)
  displayedSummary.value = ''
  let i = 0

  typingTimer = setInterval(() => {
    if (i < text.length) {
      displayedSummary.value += text[i++]
    } else {
      clearInterval(typingTimer)
    }
  }, 35)
}

watch(aiSummary, (val) => {
  if (val) {
    typeSummary(val)
    aiTags.value = extractTags(val)
  }
})

/* ========================
   AI Tags
======================== */

const extractTags = (text) => {
  return [...new Set(
    text.match(/AI|科技|商业|芯片|大模型|创业|资本|互联网/g) || []
  )]
}

/* ========================
   语音播报
======================== */

const toggleVoice = () => {
  if (!aiSummary.value) return

  if (speaking.value) {
    speechSynthesis.cancel()
    speaking.value = false
    return
  }

  utterance = new SpeechSynthesisUtterance(aiSummary.value)
  utterance.lang = 'zh-CN'
  utterance.onend = () => (speaking.value = false)
  speechSynthesis.speak(utterance)
  speaking.value = true
}

/* ========================
   文章数据
======================== */

const loadArticles = async () => {
  loading.value = true
  const res = await ArticleGetAllService({})

  articles.value = (res.data || [])
    .sort((a, b) => new Date(b.createTime) - new Date(a.createTime))

  await loadAuthors(articles.value)
  loading.value = false

  // ✅ 关键修复：等 v-for DOM 渲染完成再 observe
  await nextTick()
  observeReveal()
}

const loadAuthors = async (list) => {
  const ids = [...new Set(list.map(i => i.createUser).filter(Boolean))]
  for (const id of ids) {
    if (authorMap.value[id]) continue
    try {
      const res = await userFindById(id)
      authorMap.value[id] =
        res.data.nickname || res.data.username || `用户${id}`
    } catch {
      authorMap.value[id] = `用户${id}`
    }
  }
}

/* ========================
   点赞 / 收藏
======================== */

const toggleLike = (id) => {
  likes.value[id] = !likes.value[id]
}

const toggleFavorite = (id) => {
  favorites.value[id] = !favorites.value[id]
}

/* ========================
   AI 摘要
======================== */

const generateAiSummary = async () => {
  aiLoading.value = true
  aiSummary.value = ''
  displayedSummary.value = ''
  aiTags.value = []

  const res = await request.post('/ai/ai-news', {
    messages: [
      { role: 'user', content: '用年轻人口吻总结今日科技与商业热点' }
    ]
  })

  aiSummary.value =
    res?.data?.output?.choices?.[0]?.message?.content ||
    '今天值得慢下来，看看真正重要的事情。'

  aiLoading.value = false
}

/* ========================
   Reveal 动画
======================== */

const observeReveal = () => {
  const observer = new IntersectionObserver(entries => {
    entries.forEach(e => {
      if (e.isIntersecting) {
        e.target.classList.add('show')
        observer.unobserve(e.target)
      }
    })
  }, { threshold: 0.1 })

  document.querySelectorAll('.reveal').forEach(el => observer.observe(el))
}

/* ========================
   工具
======================== */

const formatTime = (t) => {
  const d = new Date(t)
  return `${d.getFullYear()}-${d.getMonth() + 1}-${d.getDate()}`
}

/* ========================
   生命周期
======================== */

onMounted(() => {
  loadArticles()
  generateAiSummary()
})
</script>


<style scoped>
/* ================= 页面 ================= */

.home {
  min-height: 100vh;
  background: #f7f7f8;
  color: #333;
  padding-bottom: 60px;
  font-family: -apple-system, BlinkMacSystemFont, "PingFang SC", Inter, sans-serif;
}

/* ================= AI 快讯 ================= */

.ai-focus {
  margin: 24px;
  padding: 24px;
  background: #fff;
  border-radius: 22px;
  box-shadow: 0 12px 30px rgba(0,0,0,.06);
}

.ai-header {
  display: flex;
  align-items: center;
  gap: 10px;
}

.ai-orb {
  width: 14px;
  height: 14px;
  border-radius: 50%;
  background: linear-gradient(135deg, #ff4d4f, #ff7875);
}

.ai-orb.scanning {
  animation: pulse 1.2s infinite;
}

@keyframes pulse {
  50% { opacity: .4; }
}

.ai-text {
  margin-top: 14px;
  line-height: 1.8;
  font-size: 15px;
}

.ai-loading {
  margin-top: 14px;
  color: #999;
}

.ai-tags {
  margin-top: 12px;
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.ai-tag {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 999px;
  background: #fff1f0;
  color: #ff4d4f;
}

.main-btn {
  margin-top: 18px;
  padding: 8px 22px;
  border-radius: 999px;
  border: none;
  cursor: pointer;
  background: linear-gradient(135deg, #ff4d4f, #ff7875);
  color: #fff;
  font-weight: 600;
}

.voice-btn {
  margin-left: auto;
  background: none;
  border: none;
  cursor: pointer;
}

/* ================= 主体 ================= */

.main {
  display: flex;
}

/* ================= 文章 ================= */

.article-list {
  flex: 1;
  margin: 24px;
}

.section-title {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 16px;
}

.list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.article-item {
  background: #fff;
  border-radius: 18px;
  overflow: hidden;
  transition: all .3s ease;
}

.article-item:hover {
  transform: translateY(-6px);
  box-shadow: 0 16px 32px rgba(0,0,0,.08);
}

.cover {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.content {
  padding: 14px;
}

.title-link {
  font-size: 15px;
  font-weight: 600;
  color: #222;
  text-decoration: none;
}

.title-link:hover {
  color: #ff4d4f;
}

.meta {
  margin-top: 6px;
  font-size: 12px;
  color: #999;
}

.actions {
  margin-top: 10px;
  display: flex;
  gap: 16px;
  font-size: 13px;
}

.action {
  cursor: pointer;
  color: #666;
}

.action.active {
  color: #ff4d4f;
  font-weight: 600;
}

/* ================= 热搜 ================= */

.hot-rank {
  width: 260px;
  margin: 24px 24px 0 0;
  background: #fff;
  border-radius: 18px;
  padding: 18px;
  box-shadow: 0 12px 30px rgba(0,0,0,.06);
}

.hot-title {
  margin-bottom: 14px;
  font-size: 16px;
  font-weight: 700;
}

.hot-item {
  display: flex;
  gap: 10px;
  margin-bottom: 12px;
  font-size: 13px;
}

.rank {
  width: 18px;
  text-align: center;
  font-weight: 700;
}

.rank.r0 { color: #ff4d4f; }
.rank.r1 { color: #faad14; }
.rank.r2 { color: #52c41a; }

.hot-link {
  flex: 1;
  color: #333;
  text-decoration: none;
}

.hot-link:hover {
  color: #ff4d4f;
}

.hot-views {
  font-size: 12px;
  color: #aaa;
}

/* ================= Reveal ================= */

.reveal {
  opacity: 0;
  transform: translateY(30px);
}

.reveal.show {
  opacity: 1;
  transform: none;
  transition: all .6s ease;
}
</style>