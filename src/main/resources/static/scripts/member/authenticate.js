$('#commitAuthenticateInfoBtn').click(function () {
    var jessionId=$('#authenticateInfoInput').val();
    if(jessionId==null||jessionId==''){
        $('#authenticateInfoInput').focus();
        return;
    }
    $.post("/member/authenticate",{"jsessionId":jessionId},function (result) {
        //认证成功
        if(result.code==0){
            $('#authenticateModel').modal('hide');
            document.location.reload();
        }else{//失败
            alert(result.msg);
        }
    },'json');
})