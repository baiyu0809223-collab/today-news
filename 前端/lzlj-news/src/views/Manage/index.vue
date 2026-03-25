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
const articles = ref([

])

//分页条数据模型
const pageNum = ref(1)//当前页
const total = ref(20)//总条数
const pageSize = ref(3)//每页条数

//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
  pageSize.value = size
  articleList();
}
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
  pageNum.value = num
  articleList();
}


//回显文章分类
import {
  ArticleAddService, ArticleCategoryAllListService,
  ArticleDeleteService, ArticleAllListService, ArticleUpdateService, DraftUpdateService
} from '@/api/article.js'
import { FindAllUserService } from '@/api/user.js'
const articleCategoryList = async () => {
  let result = await ArticleCategoryAllListService();
  categorys.value = result.data;
}
//获取所有用户
const articleUserList = async () => {
  let result = await FindAllUserService();
  users.value = result.data;
}
//获取文章列表数据
const articleList = async () => {
  let params = {
    page: pageNum.value,
    size: pageSize.value,
    categoryId: categoryId.value ? categoryId.value : null,
    createUser: createUser.value ? createUser.value : null,
    state: state.value ? state.value : null
  }
  let result = await ArticleAllListService(params);

  //渲染视图
  total.value = result.data.total;
  articles.value = result.data.items;

  //处理数据，给数据模型扩展一个属性CategoryName，分类名称
  for (let i = 0; i < articles.value.length; i++) {
    let article = articles.value[i];
    for (let j = 0; j < categorys.value.length; j++) {

      if (article.categoryId == categorys.value[j].id) {
        article.categoryName = categorys.value[j].categoryName;
      }
    }
  }
}


articleCategoryList();
articleList();
articleUserList();



import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import { Plus } from '@element-plus/icons-vue'
//控制抽屉是否显示
const visibleDrawer = ref(false)
//添加表单数据模型
const articleModel = ref({
  title: '',
  categoryId: '',
  createUser:'',
  coverImg: '',
  content: '',
  state: '',
  manage:''
})

//导入token
import { useTokenStore } from '@/stores/token.js';
import { ElMessage } from 'element-plus';
const tokenStore = useTokenStore();


//上传成功的回调函数
const uploadSucess = (result) => {
  articleModel.value.coverImg = result.data;
  console.log(result.data);

}

//重置搜索条件
const reset=()=>{
  categoryId.value = '';
  state.value = '';
  createUser.value = '';
}


//添加文章
const addActicle = async (clickState) => {
  //把发布状态赋值给数据模型
  articleModel.value.state = clickState;
  if(clickState=="草稿"){
    articleModel.value.manage=1
    //调用接口
    let result = await ArticleAddService(articleModel.value);
    ElMessage.success(result.msg ? result.msg : '添加成功')
  }else{
    //调用接口
    let result = await ExamineAddService(articleModel.value);
    ElMessage.success(result.msg ? result.msg : '添加成功')
  }

  visibleDrawer.value = false;

  //刷新
  articleList();
}

const title = ref('')
//修改文章
const updateActicle = async (state) => {
  if(state=="草稿"&&articleModel.value.state=="草稿"){
    let result = await DraftUpdateService(articleModel.value);
    ElMessage.success(result.msg ? result.msg : '修改成功');
  } else if(state=="草稿"){
    articleModel.value.state = "改为草稿";
    let result = await ArticleUpdateService(articleModel.value);
    ElMessage.success(result.msg ? result.msg : '修改成功');
  } else{
    //调用接口
    articleModel.value.state = state;
    let result = await ArticleUpdateService(articleModel.value);
    ElMessage.success(result.msg ? result.msg : '修改成功');
  }
  articleList();
  visibleDrawer.value = false;
}

//展示编辑弹窗
const showDialog = (row) => {
  title.value = '修改文章';
  visibleDrawer.value = true;


  articleModel.value.id = row.id;
  articleModel.value.title = row.title;
  articleModel.value.categoryId = row.categoryId;
  articleModel.value.createUser=row.createUser;
  articleModel.value.content = row.content;
  articleModel.value.coverImg = row.coverImg;
  articleModel.value.updateTime=row.updateTime;

}

//文章数据模型的清空处理
const clear = () => {
  articleModel.value.id = '';
  articleModel.value.title = '';
  articleModel.value.categoryId = '';
  articleModel.value.content = '';
  articleModel.value.coverImg = '';
  articleModel.value.content = '';
}


import { ElMessageBox } from 'element-plus';
import {ExamineAddService} from "@/api/examine.js";
//文章的删除
const deleteArticle = async (row) => {


  ElMessageBox.confirm(
      '确认删除？',
      '温馨提醒',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }
  )
      .then(async () => {
        //调用接口
        let result = await ArticleDeleteService(row.id);
        ElMessage({
          type: 'success',
          message: '已添加至审核列表，请前往确认',
        })
        //更新文章列表
        articleList();
      })
      .catch(() => {
        ElMessage({
          type: 'success',
          message: '取消删除成功',
        })
      })



}
</script>
<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>文章管理</span>
        
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

      <el-form-item label="发布状态：">
        <el-select placeholder="请选择" v-model="state" style="width: 200px;">
          <el-option label="已发布" value="已发布"></el-option>
          <el-option label="草稿" value="草稿"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="发布者：">
        <el-select placeholder="请选择" v-model="createUser" style="width: 200px;">
          <el-option v-for="c in users" :key="c.id" :label="c.username" :value="c.id">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="articleList">搜索</el-button>
        <el-button @click="reset">重置</el-button>
      </el-form-item>


    </el-form>
    <!-- 文章列表 -->
    <el-table :data="articles" style="width: 100%">
      <el-table-column label="文章标题" width="400" prop="title"></el-table-column>
      <el-table-column label="分类" prop="categoryName"></el-table-column>
      <el-table-column label="发布者" prop="createUsername"></el-table-column>
      <el-table-column label="发表时间" prop="createTime"> </el-table-column>
      <el-table-column label="状态" prop="state"></el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          
          <el-button :icon="Delete" circle plain type="danger" @click="deleteArticle(row)"></el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="没有数据" />
      </template>
    </el-table>
    <!-- 分页条 -->
    <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[3, 5, 10, 15]"
                   layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange"
                   @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />



    <!-- 抽屉 -->
    <el-drawer v-model="visibleDrawer" title="title" direction="rtl" size="50%">
      <!-- 添加文章表单 -->
      <el-form :model="articleModel" label-width="100px">
        <el-form-item label="文章标题">
          <el-input v-model="articleModel.title" placeholder="请输入标题"></el-input>
        </el-form-item>
        <el-form-item label="文章分类">
          <el-select placeholder="请选择" v-model="articleModel.categoryId">
            <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="文章封面">

          <el-upload class="avatar-uploader" :auto-upload="false" :show-file-list="false">
            <img v-if="articleModel.coverImg" :src="articleModel.coverImg" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon">
              <Plus />
            </el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="文章内容">
          <div class="editor">富文本编辑器</div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary">发布</el-button>
          <el-button type="info">草稿</el-button>
        </el-form-item>
      </el-form>
    </el-drawer><!-- 抽屉 -->
    <el-drawer v-model="visibleDrawer" :title=title direction="rtl" size="50%">
      <!-- 添加文章表单 -->
      <el-form :model="articleModel" label-width="100px">
        <el-form-item label="文章标题" prop="title">
          <el-input v-model="articleModel.title" placeholder="请输入标题"></el-input>
        </el-form-item>
        <el-form-item label="文章分类" prop="categoryId">
          <el-select placeholder="请选择" v-model="articleModel.categoryId">
            <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="文章封面" prop="coverImg">



          <!--
          auto-upload:设置是否自动上传
          action：设置服务器接口路径
          name：设置上传的文件字段名
          headers：设置上传的请求头
          on-success:设置上传成功的回调函数

          -->
          <el-upload class="avatar-uploader" :auto-upload="true" :show-file-list="false" action="/api/upload"
                     name="file" :headers="{ 'Authorization': tokenStore.token }" :on-success="uploadSucess">
            <img v-if="articleModel.coverImg" :src="articleModel.coverImg" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon">
              <Plus />
            </el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="文章内容" prop="content">
          <div class="editor">
            <quill-editor theme="snow" v-model:content="articleModel.content" contentType="html"
                          prop="content">
            </quill-editor>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"
                     @click="title == '添加文章' ? addActicle('审核') : updateActicle('审核')">发布</el-button>
          <el-button type="info" @click="title == '添加文章' ? addActicle('草稿') : updateActicle('草稿')">草稿</el-button>
        </el-form-item>
      </el-form>
    </el-drawer>
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