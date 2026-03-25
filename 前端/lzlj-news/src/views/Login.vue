<script setup>
import { User, Lock } from '@element-plus/icons-vue'
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

// api
import {
  userRegisterService,
  userLoginService,
  userInfoService,
  userAvatarService
} from '@/api/user.js'

// pinia
import { useTokenStore } from '@/stores/token.js'
import userInfoStore from '@/stores/userInfo.js'

// -------------------- state --------------------
const isRegister = ref(false)
const registerStep = ref(0) // 0: user  1: detail

const router = useRouter()
const tokenStore = useTokenStore()
const userInfo = userInfoStore()

const loginFormRef = ref()
const registerFormRef = ref()

// -------------------- form model（统一） --------------------
const registerForm = ref({
  // user
  username: '',
  password: '',
  rePassword: '',
  nickname: '',
  email: '',
  userPic: '', // ✅ 必须设置

  // detail
  bio: '',
  gender: 0,
  birthday: '',
  location: '',
  website: '',
  tags: ''
})

// 登录表单
const loginForm = ref({
  username: '',
  password: ''
})

// -------------------- rules --------------------
const checkRePassword = (rule, value, callback) => {
  if (!value) return callback(new Error('请确认密码'))
  if (value !== registerForm.value.password) {
    return callback(new Error('两次密码不一致'))
  }
  callback()
}

const registerRulesUser = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 5, max: 16, message: '5-16 位字符', trigger: 'blur' }
  ],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  rePassword: [{ validator: checkRePassword, trigger: 'blur' }],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  email: [{ required: true, type: 'email', message: '请输入正确邮箱', trigger: 'blur' }],
  userPic: [{ required: true, message: '请选择头像', trigger: 'change' }]
}

const registerRulesDetail = {
  bio: [{ required: true, message: '请输入个人简介', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  birthday: [{ required: true, message: '请选择生日', trigger: 'change' }]
}

const loginRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

// -------------------- 头像相关 --------------------
// 示例头像（你也可以换成自己 OSS 的）
const avatarList = [
  'https://api.dicebear.com/7.x/adventurer/svg?seed=1',
  'https://api.dicebear.com/7.x/adventurer/svg?seed=2',
  'https://api.dicebear.com/7.x/adventurer/svg?seed=3',
  'https://api.dicebear.com/7.x/adventurer/svg?seed=4'
]

// 选择头像
const selectAvatar = async (url) => {
  registerForm.value.userPic = url
  await userAvatarService(url)
  ElMessage.success('头像设置成功')
}

// -------------------- actions --------------------
const nextStep = async () => {
  await registerFormRef.value.validate()
  registerStep.value = 1
}

const register = async () => {
  await registerFormRef.value.validate()

  const params = {
    user: {
      username: registerForm.value.username,
      password: registerForm.value.password,
      nickname: registerForm.value.nickname,
      email: registerForm.value.email,
      userPic: registerForm.value.userPic
    },
    detail: {
      bio: registerForm.value.bio,
      gender: registerForm.value.gender,
      birthday: registerForm.value.birthday,
      location: registerForm.value.location,
      website: registerForm.value.website,
      tags: registerForm.value.tags
    }
  }

  await userRegisterService(params)
  ElMessage.success('注册成功')

  resetAll()
  isRegister.value = false
}

const login = async () => {
  await loginFormRef.value.validate()

  const res = await userLoginService(loginForm.value)
  tokenStore.setToken(res.data)

  const userRes = await userInfoService()
  userInfo.setInfo(userRes.data)

  ElMessage.success('登录成功')
  router.push('/test')
}

const resetAll = () => {
  registerStep.value = 0
  Object.assign(registerForm.value, {
    username: '',
    password: '',
    rePassword: '',
    nickname: '',
    email: '',
    userPic: '',
    bio: '',
    gender: 0,
    birthday: '',
    location: '',
    website: '',
    tags: ''
  })
  Object.assign(loginForm.value, {
    username: '',
    password: ''
  })
}
</script>

<template>
  <div class="xhs-login">
    <div class="login-card">
      <div class="logo">小红书</div>

      <div class="tab">
        <span :class="{ active: !isRegister }" @click="isRegister = false; resetAll()">登录</span>
        <span :class="{ active: isRegister }" @click="isRegister = true; resetAll()">注册</span>
      </div>

      <!-- 登录 -->
      <el-form
        v-if="!isRegister"
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        size="large"
      >
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="用户名" :prefix-icon="User" />
        </el-form-item>

        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="密码" :prefix-icon="Lock" />
        </el-form-item>

        <el-button class="submit-btn" type="primary" @click="login">
          登录
        </el-button>
      </el-form>

      <!-- 注册 -->
      <el-form
        v-else
        ref="registerFormRef"
        :model="registerForm"
        :rules="registerStep === 0 ? registerRulesUser : registerRulesDetail"
        size="large"
      >
        <!-- step1 -->
        <template v-if="registerStep === 0">
          <!-- 头像选择 -->
          <el-form-item prop="userPic">
            <div class="avatar-list">
              <img
                v-for="url in avatarList"
                :key="url"
                :src="url"
                :class="{ active: registerForm.userPic === url }"
                @click="selectAvatar(url)"
              />
            </div>
          </el-form-item>

          <el-form-item prop="username">
            <el-input v-model="registerForm.username" placeholder="用户名" />
          </el-form-item>

          <el-form-item prop="password">
            <el-input type="password" v-model="registerForm.password" placeholder="密码" />
          </el-form-item>

          <el-form-item prop="rePassword">
            <el-input type="password" v-model="registerForm.rePassword" placeholder="确认密码" />
          </el-form-item>

          <el-form-item prop="nickname">
            <el-input v-model="registerForm.nickname" placeholder="昵称" />
          </el-form-item>

          <el-form-item prop="email">
            <el-input v-model="registerForm.email" placeholder="邮箱" />
          </el-form-item>

          <el-button class="submit-btn" type="primary" @click="nextStep">
            下一步
          </el-button>
        </template>

        <!-- step2 -->
        <template v-else>
          <el-form-item prop="bio">
            <el-input type="textarea" v-model="registerForm.bio" placeholder="个人简介" />
          </el-form-item>

          <el-form-item prop="gender">
            <el-radio-group v-model="registerForm.gender">
              <el-radio :label="1">男</el-radio>
              <el-radio :label="2">女</el-radio>
              <el-radio :label="0">未知</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item prop="birthday">
            <el-date-picker v-model="registerForm.birthday" type="date" placeholder="生日" />
          </el-form-item>

          <el-form-item>
            <el-input v-model="registerForm.location" placeholder="所在地" />
          </el-form-item>

          <el-form-item>
            <el-input v-model="registerForm.website" placeholder="个人主页" />
          </el-form-item>

          <el-form-item>
            <el-input v-model="registerForm.tags" placeholder="标签（逗号分隔）" />
          </el-form-item>

          <el-button @click="registerStep--">上一步</el-button>
          <el-button class="submit-btn" type="primary" @click="register">
            完成注册
          </el-button>
        </template>
      </el-form>

      <div class="tips">
        登录/注册即表示同意
        <span>用户协议</span> 和 <span>隐私政策</span>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
$xhs-red: #ff2442;

.xhs-login {
  height: 100vh;
  background: #f7f7f7;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-card {
  width: 360px;
  padding: 32px 28px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
}

.logo {
  text-align: center;
  font-size: 28px;
  font-weight: bold;
  color: $xhs-red;
  margin-bottom: 20px;
}

.tab {
  display: flex;
  justify-content: center;
  margin-bottom: 24px;

  span {
    font-size: 18px;
    margin: 0 20px;
    cursor: pointer;
    color: #999;
    position: relative;

    &.active {
      color: #000;
      font-weight: 600;

      &::after {
        content: '';
        position: absolute;
        left: 50%;
        bottom: -6px;
        transform: translateX(-50%);
        width: 24px;
        height: 3px;
        background: $xhs-red;
        border-radius: 2px;
      }
    }
  }
}

.submit-btn {
  width: 100%;
  height: 44px;
  border-radius: 22px;
  background: $xhs-red;
  border: none;
  font-size: 16px;
}

.avatar-list {
  display: flex;
  gap: 12px;
}

.avatar-list img {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  cursor: pointer;
  border: 2px solid transparent;
}

.avatar-list img.active {
  border-color: #ff2442;
}

.tips {
  margin-top: 16px;
  font-size: 12px;
  text-align: center;
  color: #999;

  span {
    color: $xhs-red;
    cursor: pointer;
  }
}
</style>