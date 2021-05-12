layui.use(['jquery','table','form','layer'], function () {

    let $ = layui.jquery,
        table = layui.table,
        form = layui.form,
        layer = layui.layer;

    form.on('submit(search)', function (data) {
        let id = data.field.id;
        let age = data.field.age;
        table.reload('userTable', {
            where: {
                id: id,
                age: age
            }, page: {
                curr: 1 //重新从第 1 页开始
            }
        },'data');
        return false;
    });

    table.render({
        elem: '#user_table',
        url: '/admin/user/get',
        toolbar: '#toolbar_user',
        defaultToolbar: [],
        cols: [
            [
                {field: 'id', title: 'id', align: "center"},
                {field: 'name', title: '姓名', align: "center"},
                {field: 'age', title: '年龄', align: "center"},
                {fixed: 'right', title: '操作', toolbar: '#bar_user', width:200, align: "center"}
            ]
        ],
        limits: [10, 15, 20, 25, 50, 100],
        limit: 15,
        page: true,
        id: 'userTable'
    });

    table.on('toolbar(userTable)', function (obj) {
        if (obj.event === 'add') {  // 监听添加操作
            layer.open({
                title: '添加用户',
                type: 2,
                maxmin: true,
                area: ['50%', '80%'],
                content: '/admin/user/addPage',
                end: function() {
                    location.reload();
                }
            });
        }
    });

    table.on('tool(userTable)', function (obj) {
        if (obj.event === 'del') {
            let win = layer.open({
                title: '警告',
                content: '确认删除id为' + obj.data.id + '的用户吗?',
                btn: ['确认','取消'],
                yes: function () {
                    layer.close(win);
                    $.ajax({
                        url: '/admin/user/delete',
                        type: 'POST',
                        data: obj.data,
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
                                content: "后台异常，请联系管理员！"
                            });
                        }
                    });
                },
            });
        }else if (obj.event === 'edit'){
            tableCol = obj.event.data;
            let index = layer.open({
                title: '编辑用户',
                type: 2,
                maxmin: true,
                area: ['50%', '80%'],
                content: '/admin/user/editPage',
                success: function () {
                    // 获取子页面的iframe的唯一id
                    let iframe = window['layui-layer-iframe' + index];
                    iframe.layui.setField(obj.data);// 向新页面赋值参数
                },
                end: function() {
                    location.reload();
                }
            });
        }
    });

});