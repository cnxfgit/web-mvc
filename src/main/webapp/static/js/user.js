layui.use(['jquery','table'], function () {

    let $ = layui.jquery,
        table = layui.table,
        form = layui.form;

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
        url: 'user/get',
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
});