<script setup>
import {
  Edit,
  Delete
} from '@element-plus/icons-vue'

import { ref } from 'vue'

//文章分类数据模型
const categorys = ref([

])
//用户搜索时选中的分类id
const categoryId = ref('')

//用户数据模型
const users=ref('')
//用户搜索时选中的用户id
const createUser = ref('')

//用户搜索时选中的发布状态
const state = ref('')


//文章列表数据模型
const examines = ref([

])

//分页条数据模型
const pageNum = ref(1)//当前页
const total = ref(20)//总条数
const pageSize = ref(3)//每页条数

//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
  pageSize.value = size
  examineList();
}
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
  pageNum.value = num
  examineList();
}


//回显文章分类
import {
  ArticleAddService,
  ArticleCategoryAllListService, ArticleUpdateService,
} from '@/api/article.js'
import { FindAllUserService } from '@/api/user.js'
import {ExamineListService, ExamineDeleteService, ExamineDeleteArticle,ExamineUpdateArticle} from '@/api/examine.js'
const examineCategoryList = async () => {
  let result = await ArticleCategoryAllListService();
  categorys.value = result.data;
}
//获取所有用户
const examineUserList = async () => {
  let result = await FindAllUserService();
  users.value = result.data;
}
//获取文章列表数据
const examineList = async () => {
  let params = {
    page: pageNum.value,
    size: pageSize.value,
    categoryId: categoryId.value ? categoryId.value : null,
    createUser: createUser.value ? createUser.value : null,
    state: state.value ? state.value : null
  }
  let result = await ExamineListService(params);

  //渲染视图
  total.value = result.data.total;
  examines.value = result.data.items;

  //处理数据，给数据模型扩展一个属性CategoryName，分类名称
  for (let i = 0; i < examines.value.length; i++) {
    let examine = examines.value[i];
    for (let j = 0; j < categorys.value.length; j++) {

      if (examine.categoryId == categorys.value[j].id) {
        examine.categoryName = categorys.value[j].categoryName;
      }
    }
  }
}


examineCategoryList();
examineList();
examineUserList();



import '@vueup/vue-quill/dist/vue-quill.snow.css'
//添加表单数据模型
const examineModel = ref({
  id:'',
  title: '',
  categoryId: '',
  createUser:'',
  coverImg: '',
  content: '',
  state: '',
  aid:''
})

import { ElMessage } from 'element-plus';

//重置搜索条件
const reset=()=>{
  categoryId.value = '';
  state.value = '';
  createUser.value = '';
}

//通过审核
const handleApprove = async(row) => {
  examineModel.value.id=row.id;
  examineModel.value.title = row.title;
  examineModel.value.createUser=row.createUser;
  examineModel.value.categoryId = row.categoryId;
  examineModel.value.content = row.content;
  examineModel.value.coverImg = row.coverImg;
  if(row.state=="改为草稿"){
    examineModel.value.state = "草稿";
  }else{
    examineModel.value.state = "已发布";
  }
  examineModel.value.aid=row.aid;
  //调用接口
  if(row.target=="删除"){
    let result=await ExamineDeleteArticle(row.id,row.aid);
    ElMessage.success(result.msg ? result.msg : '删除成功')
  }else if(row.target=="更新"){
    let result=await ExamineUpdateArticle(examineModel.value);
    ElMessage.success(result.msg ? result.msg : '更新成功')
  }else{
    let result = await ArticleAddService(examineModel.value);
    ElMessage.success(result.msg ? result.msg : '添加成功')
  }
  //刷新
  examineList();
}


import { ElMessageBox } from 'element-plus';

//未通过审核
const handleReject = async (row) => {
  ElMessageBox.confirm(
      '确认不通过？',
      '温馨提醒',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }
  )
      .then(async () => {
        //调用接口
        let result = await ExamineDeleteService(row.id,row.aid);
        ElMessage({
          type: 'success',
          message: '拒绝请求',
        })
        //更新文章列表
        examineList();
      })
      .catch(() => {
        ElMessage({
          type: 'success',
          message: '取消成功',
        })
      })
}

const contentDialogVisible = ref(false);
const dialogContent = ref('');

const showContentDialog = (content) => {
  dialogContent.value = content;
  contentDialogVisible.value = true;
};

</script>
<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>审核列表</span>
      </div>
    </template>
    <!-- 搜索表单 -->
    <el-form inline>

      <el-form-item label="文章分类：">
        <el-select placeholder="请选择" v-model="categoryId" style="width: 200px;">
          <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id">
          </el-option>
        </el-select>
      </el-form-item>


      

      <el-form-item label="发布者：">
        <el-select placeholder="请选择" v-model="createUser" style="width: 200px;">
          <el-option v-for="c in users" :key="c.id" :label="c.username" :value="c.id">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="examineList">搜索</el-button>
        <el-button @click="reset">重置</el-button>
      </el-form-item>


    </el-form>
    <!-- 文章列表 -->
    <el-table :data="examines" style="width: 100%">
      <el-table-column label="文章标题" width="400" prop="title"></el-table-column>
      <el-table-column label="内容" width="120">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="showContentDialog(row.content)">查看全文</el-button>
        </template>
      </el-table-column>
      <el-table-column label="分类" prop="categoryName"></el-table-column>
      <el-table-column label="发布者" prop="createUsername"></el-table-column>
      <el-table-column label="发表时间" prop="createTime"> </el-table-column>
      <el-table-column label="目的" prop="target"> </el-table-column>
      <el-table-column label="状态" prop="state"></el-table-column>
      <el-table-column label="是否通过" width="100">
        <template #default="{ row }">
          <el-button :icon="Check" circle plain type="success" @click="handleApprove(row)">yes</el-button>
          <el-button :icon="Close" circle plain type="danger" @click="handleReject(row)">no</el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="没有数据" />
      </template>
    </el-table>

    <!-- 内容对话框 -->
    <el-dialog v-model="contentDialogVisible" title="文章内容" width="70%">
      <div v-html="dialogContent" style="white-space: pre-wrap; max-height: 60vh; overflow-y: auto;"></div>
      <template #footer>
        <el-button type="primary" @click="contentDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
    <!-- 分页条 -->
    <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[3, 5, 10, 15]"
                   layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange"
                   @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />

  </el-card>
</template>
<style lang="scss" scoped>
.page-container {
  min-height: 100%;
  box-sizing: border-box;

  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
}

/* 抽屉样式 */
.avatar-uploader {
  :deep() {
    .avatar {
      width: 178px;
      height: 178px;
      display: block;
    }

    .el-upload {
      border: 1px dashed var(--el-border-color);
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      transition: var(--el-transition-duration-fast);
    }

    .el-upload:hover {
      border-color: var(--el-color-primary);
    }

    .el-icon.avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 178px;
      height: 178px;
      text-align: center;
    }
  }
}

.editor {
  width: 100%;

  :deep(.ql-editor) {
    min-height: 200px;
  }
}
</style>