
// 创建通用的树结构菜单，urlData为获得数据源请求地址,// 返回类型为json// StaticResources为生成tree的位置id值
function commonTree(urlData,StaticResources,setting){
    // 创建json对象存储zTree所作的设置
    // 这里设置zTree的样式

// 准备生成树形结构的json数据(模型
    let ajaxReturn = getSynchroJSON(urlData,null);
    if(ajaxReturn.status != 200){
        layer.msg("请求处理出错了！状态响应码是："+ajaxReturn.status+"，说明是："+ajaxReturn.statusText);
        return;
    }
    // 得到属性数据
    let zTreeNodes = ajaxReturn.responseJSON.data;
    // 将树结构绑定静态资源(调用生成树形菜单的方法
    $.fn.zTree.init($("#"+StaticResources),setting,zTreeNodes);

}
// 发送ajax请求，数据类型是json字符串
function requestString(urlData,paramData,behavior){
    $.ajax({
        "url": urlData,
        "type": "post",
        "data": paramData,
        "dataType": "json",
        "success": function (response) {
            successUR(response,behavior);
        },
        "error": function (response){
            errorR(response)
        }

    })


}
// 发送ajax请求，数据类型是json字符串
function requestJSONString(urlData,paramData,behavior){
    $.ajax({
        "url": urlData,
        "type": "post",
        "data": paramData,
        "contentType": "application/json;charset=UTF-8",
        "dataType": "json",
        "success": function (response) {
            successUR(response,behavior);
        },
        "error": function (response){
            errorR(response)
        }

    })


}
// 获得json请求体，同步的
function getSynchroJSON(urlData,paramData){
    let data = $.ajax({
        "url": urlData,
        "type": "post",
        "data": paramData,
        "dataType": "json",
        "async": false

    })
    return data;

}

// 处理返回的成功请求
function successR(response){
    let result = response.result;
    if(result == "SUCCESS"){
        layer.msg("操作成功");
        // 刷新页面
        refresh();
    }
    if(result == "FAIL"){
        layer.msg("操作失败," + response.message);
    }
}
// 处理返回的错误请求
function errorR(response) {
    layer.msg("操作失败," + response.status + response.statusText);
}
// 处理返回的成功请求,不刷页面
function successUR(response,behavior){
    let result = response.result;
    if(result == "SUCCESS"){
        layer.msg(behavior+"成功");

    }
    if(result == "FAIL"){
        layer.msg(behavior+"失败," + response.message);
    }
}