$(function () {
    var pathName=window.location.pathname;
    var arr=$('#topnav li a');
    $.each($('#topnav li a'),function (i,item) {
        var href=$(item).attr('href');
        if(pathName!='/'){
            $(item).attr('href',href.replace("#","/"));
        }

    });
    console.log(arr);
})