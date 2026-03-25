import request from '@/util/request.js'


//审核列表查询
export const ExamineListService=(params)=>{
    return  request.get('/examine',{params:params})
}

//文章进入审核阶段
export const ExamineAddService=(examineData)=>{
    return request.post('/examine',examineData);
}

//未通过审核
export const ExamineDeleteService=(id,aid)=>{
    return request.delete(`/examine?id=${id}&aid=${aid}`)
}

//通过删除审核
export const ExamineDeleteArticle=(id,aid)=>{
    return request.delete(`/examine/articleDelete?id=${id}&aid=${aid}`)
}

//文章修改
export const ExamineUpdateArticle=(examineData)=>{
    return request.put('/examine',examineData);
}
//违规名称修改
export const UpdataName=(id)=>{
    return request.put('/user/updateName?id='+id);
}