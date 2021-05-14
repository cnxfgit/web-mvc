layui.use(['jquery','form','layer'], function () {

    let form = layui.form,
        layer = layui.layer,
        $ = layui.jquery;

    // 表单验证
    form.verify({

        username: function (value) {
            if (value.length < 3 || value.length > 18) {
                return '用户名的长度为3-18个字符';
            }
        },
        password:  function (value) {
            if (value.length < 3 || value.length > 18) {
                return '密码的长度为3-18个字符';
            }
        },
        verCode: function (value) {
            if (!/^(\-?)\d+$/.test(value)) {
                return '验证码只能是纯数字';
            }
        }

    });

    // 表单提交
    form.on('submit(login)', function (data) {

        $.ajax({
            url: '/admin/login',
            type: 'POST',
            data: data.field,
            success: function (result) {
                if (result.code === 200){
                    layer.open({
                        title: '后台正常',
                        content: result.msg
                    });
                }else {
                    flushCode();
                    layer.open({
                        title: '后台正常',
                        content: result.msg
                    });
                }
            },
            error: function (result) {
                flushCode();
                layer.open({
                    title: '后台异常',
                    content: "后台异常，请联系管理员！"
                });
            }
        });

        return false;// 不刷新页面
    });

    // 看不清 换一张
    $("#verCodeImg").click(function () {
        flushCode();
    });

    // 刷新验证码
    function flushCode() {
        let code = $("#verCodeImg");
        code.prop('src', '/admin/captcha' + "?" + Date.now());
    }

});