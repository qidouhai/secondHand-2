//textarea高度自适应
$(function () {
    $('.content').flexText();
});

//textarea限制字数
function keyUP(t) {
    var len = $(t).val().length;
    if (len > 139) {
        $(t).val($(t).val().substring(0, 140));
    }
}


//初始化评论数据
function initCommentData() {
    //清空已有评论数据
    $('.comment-show').empty();
    //向后台请求新数据
    $.get('/comment', {"goodsId": $('#entityId').val()}, function (result) {
        if (result.code == 0) {//异步请求成功到有效数据
            $.each(result.msg, function (i, item) {//result.msg
                //创建评论条
                var comment = item.main;//result.msg.main
                var headUrl = "http://secondhand-oss.oss-cn-beijing.aliyuncs.com/" + comment.fromUser.headUrl + "?x-oss-process=style/48_size";
                var content = comment.content;
                var created = comment.created;
                oHtml = '<div class="comment-show-con clearfix" conversationId="' + comment.id + '"><div class="comment-show-con-img pull-left"><img src="' + headUrl + '" alt=""/></div> <div class="comment-show-con-list pull-left clearfix"><div class="pl-text clearfix"> <a href="/user/' + comment.fromUser.id + '" class="comment-size-name">' + comment.fromUser.name + ' : </a> <span class="my-pl-con">&nbsp;' + content + '</span> </div> <div class="date-dz"> <span class="date-dz-left pull-left comment-time">' + created + '</span> <div class="date-dz-right pull-right comment-pl-block"><a href="javascript:;" class="removeBlock hidden">删除</a> <a href="javascript:;" class="date-dz-pl pl-hf hf-con-block pull-left" userId="' + comment.fromUser.id + '">回复</a> <span class="pull-left date-dz-line hidden">|</span> <a href="javascript:;" class="date-dz-z pull-left hidden"><i class="date-dz-z-click-red"></i>赞 (<i class="z-num">666</i>)</a> </div> </div><div class="hf-list-con"></div></div> </div>';

                if (comment.content.replace(/(^\s*)|(\s*$)/g, "") != '') {
                    $('.comment-show').append(oHtml);
                    $('.commentAll').siblings('.flex-text-wrap').find('.comment-input').prop('value', '').siblings('pre').find('span').text('');
                }
                ;
                //创建评论回复块
                $.each(item.reply, function (i, item) {
                    var comment = item;//result.msg.main
                    var headUrl = "http://secondhand-oss.oss-cn-beijing.aliyuncs.com/" + comment.fromUser.headUrl + "?x-oss-process=style/48_size";
                    var from_name = comment.fromUser.name;
                    var to_name = comment.toUser.name;
                    var content = comment.content;
                    var created = comment.created;
                    var oAt = ' 回复 <a href="/user/' + comment.toUser.id + '" class="atName">@' + to_name + '</a> : ' + content;
                    var oHtml = '<div class="all-pl-con"><div class="pl-text hfpl-text clearfix"><a href="/user/' + comment.fromUser.id + '" class="comment-size-name">' + from_name + ' : </a><span class="my-pl-con">' + oAt + '</span></div><div class="date-dz"> <span class="date-dz-left pull-left comment-time">' + created + '</span> <div class="date-dz-right pull-right comment-pl-block"> <a href="javascript:;" class="removeBlock hidden">删除</a> <a href="javascript:;" class="date-dz-pl pl-hf hf-con-block pull-left" userId="' + comment.fromUser.id + '">回复</a> <span class="pull-left date-dz-line hidden">|</span> <a href="javascript:;" class="date-dz-z pull-left hidden"><i class="date-dz-z-click-red"></i>赞 (<i class="z-num">666</i>)</a> </div> </div></div>';
                    $('.comment-show-con:last').find('.hf-list-con').css('display', 'block').prepend(oHtml);

                })
            });
        }
    }, 'json');
}

//点击评论创建评论条
$('.commentAll').on('click', '.plBtn', function () {
    if($('input[name=curUserId]').val()==0){
        alert('请先登录');
        return ;
    }

    var myDate = new Date();
    //获取当前年
    var year = myDate.getFullYear();
    //获取当前月
    var month = myDate.getMonth() + 1;
    //获取当前日
    var date = myDate.getDate();
    var h = myDate.getHours();       //获取当前小时数(0-23)
    var m = myDate.getMinutes();     //获取当前分钟数(0-59)
    if (m < 10) m = '0' + m;
    var s = myDate.getSeconds();
    if (s < 10) s = '0' + s;
    var now = year + '-' + month + "-" + date + " " + h + ':' + m + ":" + s;
    //获取输入内容
    var oSize = $(this).siblings('.flex-text-wrap').find('.comment-input').val();
    console.log(oSize);
   //提交评论
    var entityId = $('#entityId').val();
    var entityType = $('#entityType').val();
    var toUserId = $('#userId').val();
    $.post(
        "/comment/",
        {
            'content': oSize,
            'parentId': 0,
            'entityId': entityId,
            'entityType': entityType,
            'fromUser.id': $('input[name=curUserId]').val(),
            'toUser.id': toUserId
        },
        function (result) {
            if(result.code==1){//评论创建失败
                alert(result.msg);
            }else{
                //清除评论框数据
                $('.reviewArea').find('.comment-input').val('');
                //重新初始化评论数据
                initCommentData();

            }
        },
        'json');
});
//点击回复动态创建回复块
$('.comment-show').on('click', '.pl-hf', function () {
    if($('input[name=curUserId]').val()==0){
        alert('请先登录');
        return ;
    }

    //获取回复人的名字
    var fhName = $(this).parents('.date-dz-right').parents('.date-dz').siblings('.pl-text').find('.comment-size-name').html();
    //回复@
    var fhN = '回复@' + fhName;
    //var oInput = $(this).parents('.date-dz-right').parents('.date-dz').siblings('.hf-con');
    var fhHtml = '<div class="hf-con pull-left"> <textarea class="content comment-input hf-input" placeholder="" onkeyup="keyUP(this)"></textarea> <a href="javascript:;" class="hf-pl">评论</a></div>';
    //显示回复
    if ($(this).is('.hf-con-block')) {
        $(this).parents('.date-dz-right').parents('.date-dz').append(fhHtml);
        $(this).removeClass('hf-con-block');
        $('.content').flexText();
        $(this).parents('.date-dz-right').siblings('.hf-con').find('.pre').css('padding', '6px 15px');
        //console.log($(this).parents('.date-dz-right').siblings('.hf-con').find('.pre'))
        //input框自动聚焦
        $(this).parents('.date-dz-right').siblings('.hf-con').find('.hf-input').val('').focus().attr("placeholder",fhN);
        $(this).parents('.date-dz-right').siblings('.hf-con').find('.hf-input').change(function () {
            $(this).val()
            var reg=/^/;
        });
    } else {
        $(this).addClass('hf-con-block');
        $(this).parents('.date-dz-right').siblings('.hf-con').remove();
    }
});
//评论回复块创建
$('.comment-show').on('click', '.hf-pl', function () {
    if($('input[name=curUserId]').val()==0){
        alert('请先登录');
        return ;
    }
    var oThis = $(this);
    console.log(oThis);
    var myDate = new Date();
    //获取当前年
    var year = myDate.getFullYear();
    //获取当前月
    var month = myDate.getMonth() + 1;
    //获取当前日
    var date = myDate.getDate();
    var h = myDate.getHours();       //获取当前小时数(0-23)
    var m = myDate.getMinutes();     //获取当前分钟数(0-59)
    if (m < 10) m = '0' + m;
    var s = myDate.getSeconds();
    if (s < 10) s = '0' + s;
    var now = year + '-' + month + "-" + date + " " + h + ':' + m + ":" + s;
    //获取输入内容
    var oHfVal = $(this).siblings('.flex-text-wrap').find('.hf-input').val();
    var oHfName = $(this).parents('.hf-con').parents('.date-dz').siblings('.pl-text').find('.comment-size-name').html();
    var oAllVal = '回复@' + oHfName+' :';
    //输入内容首尾空格去除trim()
    oHfVal=$.trim(oHfVal);
    var reg = new RegExp("^回复@"+oHfName+" : .*\\S$");
    if (oHfVal.replace(/^ +| +$/g, '') == '' || oHfVal == oAllVal) {
        console.log("无效输入");
    } else {//验证内容为有效数据
        console.log('conversationId:' + oThis.parents('.hf-con').parents('.comment-show-con').attr('conversationId'));
        console.log('toUserId:' + oThis.parents('.hf-con').parents('.date-dz').find('.comment-pl-block').find('.date-dz-pl').attr('userId'));
        //当前评论块的会话id
        var conversationId = oThis.parents('.hf-con').parents('.comment-show-con').attr('conversationId');
        //被追评人id
        var toUserId = oThis.parents('.hf-con').parents('.date-dz').find('.comment-pl-block').find('.date-dz-pl').attr('userId');
        var entityId = $('#entityId').val();
        var entityType = $('#entityType').val();
        $.post(
            "/comment/",
            {
                'content': oHfVal,
                'parentId': conversationId,
                'entityId': entityId,
                'entityType': entityType,
                'fromUser.id': $('input[name=curUserId]').val(),
                'toUser.id': toUserId
            },
            function (result) {
                if(result.code==1){//评论创建失败
                    alert(result.msg);
                }else{
                    //重新初始化评论数据
                    initCommentData();
                }
            },
            'json');

    }
});
//删除评论块
$('.commentAll').on('click', '.removeBlock', function () {
    var oT = $(this).parents('.date-dz-right').parents('.date-dz').parents('.all-pl-con');
    if (oT.siblings('.all-pl-con').length >= 1) {
        oT.remove();
    } else {
        $(this).parents('.date-dz-right').parents('.date-dz').parents('.all-pl-con').parents('.hf-list-con').css('display', 'none')
        oT.remove();
    }
    $(this).parents('.date-dz-right').parents('.date-dz').parents('.comment-show-con-list').parents('.comment-show-con').remove();

})
//点赞
$('.comment-show').on('click', '.date-dz-z', function () {
    var zNum = $(this).find('.z-num').html();
    if ($(this).is('.date-dz-z-click')) {
        zNum--;
        $(this).removeClass('date-dz-z-click red');
        $(this).find('.z-num').html(zNum);
        $(this).find('.date-dz-z-click-red').removeClass('red');
    } else {
        zNum++;
        $(this).addClass('date-dz-z-click');
        $(this).find('.z-num').html(zNum);
        $(this).find('.date-dz-z-click-red').addClass('red');
    }
})

