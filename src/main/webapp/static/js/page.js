function page(env) {
    var $wrapper = $('.center');
    var $detail = $('.detail');
    var $search_result = $('.search_result');

    $wrapper.spinModal(true);
    var url = "/search.do";
    //event.preventDefault(); // 使a自带的方法失效，即无法向addStudent.action发出请求
    var proms = env.id.split("&");
    var keyWords = proms[0];
    var pageNum = proms[1];
    var dataObject = {"keyWords":keyWords, "pageNum":pageNum};
    console.log(JSON.stringify(dataObject));
    $.ajax({
        async: false,
        type: "GET", // 使用post方式
        url: url,
        contentType: "application/x-www-form-urlencoded", //默认方式前台直接可以获取
        data: dataObject, // 参数列表，stringify()方法用于将JS对象序列化为json字符串
        success: function (result) {
            $wrapper.spinModal(false);
            $search_result.html("");
            $search_result.html($(result));
        },
        error: function () {
            $wrapper.spinModal(false);
            // 请求失败后的操作
            alert("查询详情失败");
        }
    });
}