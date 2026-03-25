<script setup>
import { Plus } from '@element-plus/icons-vue'
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import avatar from '@/assets/default.png'

// api
import { userRegisterService, userAvatarService } from '@/api/user'

// pinia
import userInfoStore from '@/stores/userInfo'
const user = userInfoStore()

/* ================== form ================== */
const registerFormRef = ref()

const registerForm = ref({
  username: '',
  password: '',
  rePassword: '',
  nickname: '',
  email: '',
  userPic: ''   // ✅ 头像 URL
})

/* ================== avatar upload ================== */
const uploadRef = ref()
const imgUrl = ref(avatar)

// 上传成功
const uploadSuccess = (result) => {
  if (!result || !result.data) {
    ElMessage.error('上传失败')
    return
  }
  imgUrl.value = result.data
  registerForm.value.userPic = result.data
}

// 上传头像（调用 updateAvatar）
const updateAvatar = async () => {
  if (!imgUrl.value || imgUrl.value === avatar) {
    ElMessage.warning('请先选择图片')
    return
  }

  try {
    await userAvatarService({
      avatar: imgUrl.value
    })
    ElMessage.success('头像设置成功')
  } catch (err) {
    ElMessage.error(err?.msg || '头像设置失败')
  }
}

/* ================== register ================== */
const register = async () => {
  if (!registerForm.value.userPic) {
    ElMessage.warning('请先上传头像')
    return
  }

  if (registerForm.value.password !== registerForm.value.rePassword) {
    ElMessage.error('两次密码不一致')
    return
  }

  try {
    await userRegisterService(registerForm.value)
    ElMessage.success('注册成功')
  } catch (err) {
    ElMessage.error(err?.msg || '注册失败')
  }
}
</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>用户注册</span>
      </div>
    </template>

    <el-row>
      <el-col :span="12">

        <!-- 头像上传 -->
        <el-upload
          ref="uploadRef"
          class="avatar-uploader"
          action="/api/upload"
          name="file"
          :show-file-list="false"
          :auto-upload="true"
          :on-success="uploadSuccess"
        >
          <img v-if="imgUrl" :src="imgUrl" class="avatar" />
          <img v-else :src="avatar" class="avatar" />
        </el-upload>

        <br />

        <el-button
          type="primary"
          :icon="Plus"
          size="large"
          @click="uploadRef.$el.querySelector('input').click()"
        >
          选择图片
        </el-button>

        <el-button
          type="success"
          size="large"
          @click="updateAvatar"
        >
          上传头像
        </el-button>

        <br /><br />

        <!-- 注册表单 -->
        <el-form ref="registerFormRef" :model="registerForm" label-width="80px">
          <el-form-item label="用户名">
            <el-input v-model="registerForm.username" />
          </el-form-item>

          <el-form-item label="密码">
            <el-input type="password" v-model="registerForm.password" />
          </el-form-item>

          <el-form-item label="确认密码">
            <el-input type="password" v-model="registerForm.rePassword" />
          </el-form-item>

          <el-form-item label="昵称">
            <el-input v-model="registerForm.nickname" />
          </el-form-item>

          <el-form-item label="邮箱">
            <el-input v-model="registerForm.email" />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="register">
              注册
            </el-button>
          </el-form-item>
        </el-form>

      </el-col>
    </el-row>
  </el-card>
</template>

<style lang="scss" scoped>
.avatar-uploader {
  :deep() {
    .avatar {
      width: 278px;
      height: 278px;
      display: block;
    }

    .el-upload {
      border: 1px dashed var(--el-border-color);
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
    }

    .el-upload:hover {
      border-color: var(--el-color-primary);
    }
  }
}
</style>