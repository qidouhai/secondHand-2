$('#shopTable').bootstrapTable({
    url: '/cart/list',
    method: 'get',
    columns: [{
        field: 'goodsName',
        title: '商品名称',
        formatter: function (value, row, index) {
            var url=row.status==2?'/goods/preview/'+row.id:'/goods/'+row.id;
            return '  <div class="sm_img"><a href="'+url+'" style="margin-top: 0px" target="_blank"><img src="http://secondhand-oss.oss-cn-beijing.aliyuncs.com/'+row.goodsImage+'?x-oss-process=style/48_size" width="48" height="48" style="margin-top: 0px"></a></div>\n' +
                ' <a href="'+url+'" style="font-size: 12px;" target="_blank">'+row.goodsName+'</a>';
        }
    }, {
        field: 'goodsPrice',
        title: '商品单价(￥)',
        width:200,
        formatter: function (value, row, index) {
            return (row.goodsPrice * 1.0) / 100;
        }
    }, {
        field: 'num',
        title: '数量(件)',
        width:200
    },{
        field: '',
        title: '操作',
        width:170,
        formatter: function (value, row, index) {
            if (row.status == 1) {
                return "<a href='/goods/cancelPublish/" + row.id + "' style='margin-right: 20px'>取消发布</a>" +
                    "<a href='javascript:void(0)' onclick='alert(\"编辑前需要取消发布\")' style='margin-right: 20px'>编辑</a>" +
                    "<a href='javascript:void(0)' onclick='alert(\"删除前需要取消发布\")'>删除</a>";
            } else if (row.status == 2) {
                return "<a href='/goods/cancelPublish/" + row.id + "' style='margin-right: 20px'>立即发布</a>" +
                    "<a href='/goods/edit/" + row.id + "' style='margin-right: 20px'>编辑</a>" +
                    "<a href='/goods/delete/" + row.id + "'>删除</a>";
            }
        }
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
            'searchText':'',
            'sortName': '',
            'sortOrder':''
        };
    },
    pageSize: 7

});


