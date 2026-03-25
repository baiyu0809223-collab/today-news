<script lang="ts" setup>
import {
  Warning
} from '@element-plus/icons-vue'
import { FindAllUserService } from '@/api/user'
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { UpdataName } from '@/api/examine';

const search = ref('')

const userList=ref([]);


const findAllUser=async()=>{
    let result=await FindAllUserService();
    userList.value=result.data;
}
findAllUser();


// 打开编辑对话框
const updataName =async (user) => {
  try{
    let result=await UpdataName(user.id);
    ElMessage.success(result.msg?result.msg:'修改违规名称成功')
    findAllUser();
  }catch (error) {
    ElMessage.error('修改违规名称失败')
  }
}


// const userInfo=()=>{
//     let result=await FindUserByStatusService();
    
// }
</script>


<template>
    
    <el-card style="max-width: 100%">
    <template #header>
      <div class="card-header">
        <span>用户管理</span>
        
      </div>
    </template>
    <el-table :data="userList" style="width: 100%">
    <el-table-column prop="username" label="用户账号"/>
    <el-table-column prop="nickname" label="用户名称"/>
    <el-table-column  label="精品图片">
        <template v-slot="scope">
            <img :src="scope.row.userPic" style="width: 50px;height:50px"/>
        </template>
    </el-table-column>
    <el-table-column prop="createTime" label="用户创建时间"/>
    <el-table-column prop="updateTime" label="用户更新时间"/>
    <el-table-column prop="email" label="用户邮箱地址"/>
    <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button :icon="Warning" circle plain type="danger" @click="updataName(row)"></el-button>
        </template>
    </el-table-column>
  </el-table>
  </el-card>

</template>
<style>
</style>
