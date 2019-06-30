$('#shopTable').bootstrapTable({
    toolbar:'#toolbar',
    url: '/member/mygoods',
    method: 'post',
    columns: [
        {
        field: 'goodsName',
        title: '商品名称',
        formatter: function (value, row, index) {
            var url=row.status==2?'/goods/preview/'+row.id:'/goods/'+row.id;
            img=row.images.split(";")[0];
            return '  <div class="sm_img"><a href="'+url+'" style="margin-top: 0px" target="_blank"><img src="http://secondhand-oss.oss-cn-beijing.aliyuncs.com/'+img+'?x-oss-process=style/48_size" width="48" height="48" style="margin-top: 0px"></a></div>\n' +
                ' <a href="'+url+'" style="font-size: 12px;" target="_blank">'+row.goodsName+'</a>';
        }
    }, {
        field: 'price',
        title: '商品价格(￥)',
        width:200,
        formatter: function (value, row, index) {
            return (row.price * 1.0) / 100;
        }
    }, {
        field: '',
        title: '操作',
        width:170,
        formatter:formatterOperation
    }],
    pagination: true,
    sidePagination: 'server',
    search: true,
    trimOnSearch:false,
    sortOrder: 'desc',
    contentType:'application/x-www-form-urlencoded',
    queryParams: function (params) {
        console.log(params);
        return {
            'pageSize': params.limit,
            'pageNumber': (params.offset / params.limit) + 1,
            'searchText': params.search,
            'sortName': '',
            'sortOrder': $('#toolbar .order a[class=active]').attr('value'),
            'status':$('#toolbar .filter a[class=active]').attr('value')
        };
    },
    pageSize: 7

});


function formatterOperation(value, row, index) {
    if (row.status == 1) {
        return "<input type='hidden' value='"+row.id+"'/><a href='javascript:void(0)' onclick='cancelPublish("+row.id+")'  style='margin-right: 20px'>取消发布</a>" +
            "<a href='javascript:void(0)' onclick='alert(\"编辑前需要取消发布\")' style='margin-right: 20px'>编辑</a>" +
            "<a href='javascript:void(0)' onclick='alert(\"删除前需要取消发布\")'>删除</a>";
    } else if (row.status == 2) {
        return "<input type='hidden' value='\"+row.id+\"'/><a href='javascript:void(0)' onclick='publish("+row.id+")' style='margin-right: 20px'>立即发布</a>" +
            "<a href='/goods/preUpdate/" + row.id + "' style='margin-right: 20px' target='_blank'>编辑</a>" +
            "<a href='/goods/delete/" + row.id + "'>删除</a>";
    }
}

function cancelPublish(goodsId){
    $.ajax({
        url:'/goods/cancelPublish/'+goodsId,
        type:"post",
        dataType:'json',
        success:function (result) {
            if(result.code==0){

                $('#shopTable').bootstrapTable('refresh');
            }else{
                alert(result.msg);
            }
        }
    });
}

function publish(goodsId){
    $.ajax({
        url:'/goods/publish/'+goodsId,
        type:"post",
        dataType:'json',
        success:function (result) {
            if(result.code==0){

                //刷新表格数据
                $('#shopTable').bootstrapTable('refresh');
            }else{
                alert(result.msg);
            }
        }
    });
}

