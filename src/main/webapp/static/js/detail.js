function detail(env) {
    var $wrapper = $('.center');
    var $detail = $('.detail');
    var $search_result = $('.search_result');
    //event.preventDefault(); // 使a自带的方法失效，即无法向addStudent.action发出请求
    var id = env.id;
    $wrapper.spinModal(true);
    var url = "/detailDocById.do";
    //JSON.stringify({param1:value1, param2:value2})
    var data = {"id":id};
    $.ajax({
        async: false,
        type: "GET", // 使用post方式
        url: url,
        //contentType: "application/json",//必须为@RequestBody
        contentType: "application/x-www-form-urlencoded", //默认方式前台直接可以获取
        data: data, // 参数列表，stringify()方法用于将JS对象序列化为json字符串
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