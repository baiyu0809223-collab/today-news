<script setup>
import { User, Lock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus';
import { ref } from 'vue'
//控制注册与登录表单的显示， 默认显示注册
const isRegister = ref(true)

const resestpasswordData=ref({
    old_pwd:'',
    new_pwd:'',
    re_pwd:''
})


//定义表但校验规则
// const rules={
//     newpwd:[
//         {required:true,message:'请输入密码',trigger:'blur'},
//         {min:5,max:16,message:'请输入5-16位非空字符的密码',trigger:'blur'}
//     ],
//     repwd:[
//         {validator:checkRePassword,trigger:'blur'}
//     ]
// }


import { userPasswordService } from '@/api/user';
//修改密码
const reset=async()=>{
    let result=await userPasswordService(resestpasswordData);
    ElMessage.success(result.msg?result.msg:'修改成功')
}
</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>基本资料</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form :model="resestpasswordData" :rules="rules" label-width="100px" size="large">
                    <el-form-item label="原密码">
                        <el-input v-model="resestpasswordData.old_pwd"></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" prop="nickname">
                        <el-input v-model="resestpasswordData.new_pwd"></el-input>
                    </el-form-item>
                    <el-form-item label="再次输入新密码" prop="email">
                        <el-input v-model="resestpasswordData.re_pwd"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="reset">提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>