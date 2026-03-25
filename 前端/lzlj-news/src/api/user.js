import request from '@/util/request.js'

/* ===========================
   用户基础接口
   =========================== */

// ✅ 注册（user + userDetail）
export const userRegisterService = (registerData) => {
  // registerData:
  // {
  //   user: {...},
  //   detail: {...}
  // }
  return request.post('/user/register', registerData)
}

// 登录
export const userLoginService = (loginData) => {
  const params = new URLSearchParams()
  for (let key in loginData) {
    params.append(key, loginData[key])
  }
  return request.post('/user/login', params)
}

// 获取当前登录用户信息
export const userInfoService = () => {
  return request.get('/user/userInfo')
}

// 修改用户基础信息
export const userUpdateService = (userInfoData) => {
  return request.put('/user/update', userInfoData)
}

// 修改头像
export const userAvatarService = (avatarUrl) => {
  const params = new URLSearchParams()
  params.append('avatarUrl', avatarUrl)
  return request.patch('/user/updateAvatar', params)
}

// 修改密码
export const userPasswordService = (resetPasswordData) => {
  return request.patch('/user/updatePwd', resetPasswordData)
}

// 根据用户 ID 获取用户（管理员）
export const userFindById = (id) => {
  return request.get('/user/getUserById?id=' + id)
}

// 获取所有用户（管理员）
export const FindAllUserService = () => {
  return request.get('/user/getAllUser')
}

/* ===========================
   用户个人名片（user_detail）
   =========================== */

// 新增用户个人名片
export const createUserDetailService = (userDetailData) => {
  return request.post('/user/detail', userDetailData)
}

// 根据 userId 获取个人名片
export const getUserDetailByUserIdService = (userId) => {
  return request.get('/user/detail', {
    params: { userId }
  })
}

// 更新用户个人名片
export const updateUserDetailService = (userDetailData) => {
  return request.put('/user/detail', userDetailData)
}

// 删除用户个人名片
export const deleteUserDetailService = (userId) => {
  return request.delete('/user/detail', {
    params: { userId }
  })
}