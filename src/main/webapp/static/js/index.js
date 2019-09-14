$(document).ready(function(){
    var $wrapper = $('.center');
    var $search_result = $('.search_result');
    var $apply_link_form = $('#apply_link_form');

    //ajax提交信息
    $apply_link_form.on("click",function(){
        var keyWords = $('#keyWords').val();
        var pageNum = $('#pageNum').val();
        //json对象
        var dataObject = {"keyWords":keyWords, "pageNum":pageNum};
        //json字符串
        var data = JSON.stringify(dataObject);
        $wrapper.spinModal(true);

        $.ajax({
            async: false,
            type: "GET",
            url:'/search.do',
           //（1）当Ajax以application/x-www-form-urlencoded格式上传即使用JSON对象，后台需要使用@RequestParam 或者Servlet获取也可以不写，默认。
           //（2） 当Ajax以application/json格式上传即使用JSON字符串，后台需要使用@RquestBody获取。
            contentType: "application/x-www-form-urlencoded", //默认方式前台直接可以获取
            data: dataObject ,
            success: function (result) {
                //关闭遮罩
                $wrapper.spinModal(false);
                $search_result.html("");
                $search_result.html($(result));
            },
            error: function () {
                //关闭遮罩
                $wrapper.spinModal(false);
                alert("查询异常");
            }
        });
    });
});