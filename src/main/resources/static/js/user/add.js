layui.use(['jquery','form','layer'], function () {

    let $ = layui.jquery,
        form = layui.form,
        layer = layui.layer;

    // 提交表单
    form.on('submit(saveBtn)', function (data) {
        $.ajax({
            url: '/user/add',
            type: 'POST',
            data: data.field,
            success: function (result) {
                if (result.code === 200){
                    layer.open({
                        title: '后台正常',
                        content: result.msg
                    });
                }else {
                    layer.open({
                        title: '后台正常',
                        content: result.msg
                    });
                }
            },
            error: function (result) {
                layer.open({
                    title: '后台异常',
                    content: result.msg
                });
            }
        });

        return false;
    });


});