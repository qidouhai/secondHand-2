$(function () {
    var tag_arr=$('.tags');
    $.each(tag_arr,function (i,obj) {
        console.log(i);
        var ul=$(obj).find('ul')[0];
        $.each($(ul).children(),function (i,tag) {
            var rand_num=parseInt(100*i*Math.random());
            var index=rand_num%color_arr.length;
            var cur_color=color_arr[index];
            console.log(index);
            $(tag).css("background-color",cur_color);
            $(tag).css("border-color",cur_color);
        })
    });

});

var color_arr=new Array('#FFC125','#FFB6C1','#FFA500','#FF83FA','#FF7F24','#FF6A6A','##FF3E96','#FF6347','#EE799F','#DB7093');
console.log('length:',color_arr.length);