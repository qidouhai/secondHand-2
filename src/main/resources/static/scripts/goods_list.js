//重设样式
function resetHighlight(obj) {
    var ul=$(obj).parent().parent();
    $(ul.find('a[class=highlight]')).removeClass("highlight");
    $(obj).addClass("highlight");
    //如果类目切换样式
    if(ul.attr("value")=='category'){
        //清除子类目条件样式
        resetHighlight($('.sub_category li:eq(0) a'));
    }
}

function switchSelected(obj) {
    resetHighlight(obj);
    var categoryId = $($('.category:eq(0)').find('a[class=highlight]')).attr('value');
    var subCategoryId = 0;
    if (categoryId != 0) {
        subCategoryId = $($('.sub_category:eq(0)').find('a[class=highlight]')).attr('value');
    }
    var orderName = $($('.order:eq(0)').find('a[class=highlight]')).attr('name');
    var orderValue = $($('.order:eq(0)').find('a[class=highlight]')).attr('value');
    console.log("categoryId:" + categoryId);
    console.log("subCategoryId:" + subCategoryId);
    console.log("orderName:" + orderName);
    console.log("orderValue:" + orderValue);
    var url = "/goods/list/";
    categoryId > 0 ? url += categoryId + '/' : url;
    subCategoryId > 0 ? url += subCategoryId : url;
    url +='?orderName='+orderName;
    if(orderName!='hot'){
        url+='&orderValue='+orderValue
    }
    console.log(url);
    window.location.href=url;
}

//换页
function goPage(curPage){
    var href=window.location.href;
    if(href.indexOf('?')>0){
        var index=href.indexOf('curPage');
        if(index>0){
            href=href.substring(0,index);
        }
       if(index==href.indexOf('?')+1){
           href+='curPage='+curPage;
       }else{
           href+='&curPage='+curPage;
       }

    }else{
        href+='?curPage='+curPage;
    }
    window.location.href=href;
}