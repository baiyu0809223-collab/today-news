import request from'@/util/request.js'
import {useTokenStore}from'@/stores/token.js'
//文章分类查询
export const ArticleCategoryListService=()=>{
    //const token=useTokenStore();
    //在pinia中定义的响应式数据，都不需要value
    //return request.get('/category',{headers:{'Authorization':token.token}})
    return request.get('/category')
}
//所有文章分类
export const ArticleCategoryAllListService=()=>{
    //const token=useTokenStore();
    //在pinia中定义的响应式数据，都不需要value
    //return request.get('/category',{headers:{'Authorization':token.token}})
    return request.get('/category/all')
}
//添加分类
export const ArticleAddCategoryService=(categoryData)=>{

    return request.post('/category',categoryData)

}

//文章分类修改
export const ArticleCategoryUpdateService=(categoryData)=>{

    return request.put('/category',categoryData)

}

//文章分类删除
export const ArticleCategoryDeleteService=(id)=>{
    return request.delete('/category?id='+id)
}


//文章列表查询
export const ArticleListService=(params)=>{
    return  request.get('/article',{params:params})
}

//文章所有列表查询
export const ArticleAllListService=(params)=>{
    return  request.get('/article/alllist',{params:params})
}

//文章添加
export const ArticleAddService=(articleData)=>{
    return request.post('/article',articleData);
}

//文章修改
export const ArticleUpdateService=(articleData)=>{
    return request.put('/article',articleData);
}

//文章删除
export const ArticleDeleteService=(articleid)=>{
    return request.delete('/article?id='+articleid)
}
//获取所有文章
export const ArticleGetAllService=(params)=>{
    return request.get('/article/all',{params:params})
}
//获取详细文章
export const ArticleGetDetailService=(id)=>{
    return request.get('/article/detail?id='+id);
}


// 评论添加
export const CommentAddService = (commentData) => {
    return request.post('/comment', commentData);
};

// 评论列表
export const CommentListService = (article_id, page, pageSize, sortType) => {
    return request.get(`/comment/article/${article_id}`, {
        params: {
            page: Number(page) || 1,
            pageSize: Number(pageSize) || 10,
            sortType: sortType || 'time'
        }
    });
};

// 删除评论
export const CommentDeleteService = (comment_id) => {
    return request.delete(`/comment/${comment_id}`);
};

// 点赞/取消点赞
export const CommentLikeService = (comment_id) => {
    return request.post(`/comment/${comment_id}/like`);
};

// 检查是否点赞
export const CommentIsLikedService = (comment_id) => {
    return request.get(`/comment/${comment_id}/isLiked`);
};

// 获取点赞数
export const CommentGetLikeCountService = (comment_id) => {
    return request.get(`/comment/${comment_id}/likes/count`);
};

// 获取回复列表
export const CommentGetRepliesService = (parent_id) => {
    return request.get(`/comment/${parent_id}/replies`);
};
//草稿修改
export const DraftUpdateService=(articleData)=>{
    return request.put('/article/draftUpdata',articleData);
};
//草稿删除
export const DraftDeleteService=(id)=>{
    return request.delete('/article/draftDelete?id='+id)
};

