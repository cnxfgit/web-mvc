layui.use(['jquery','table'], function () {

    let $ = layui.jquery,
        table = layui.table;

    table.render({
        elem: '#user_table',
        url: '/user/get',
        toolbar: '#toolbarDemo',
        cols: [
            [
                {field: 'id', title: 'id'},
                {field: 'name', title: '姓名'},
                {field: 'age', title: '年龄'}
            ]
        ],
        limits: [10, 15, 20, 25, 50, 100],
        limit: 15,
        page: true,
    });
});