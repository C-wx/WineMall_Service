layui.use(['form', 'table'], function () {
    var form = layui.form,
        table = layui.table;
    form.render();

    /**
     * 初始化表格
     */
    var productRecycleTable = table.render({
        elem: '#productRecycleTable'
        , url: '/productRecyclePage'
        , page: true
        , limit: 10
        , height: 'full'
        , method: 'get'
        , request: {
            pageName: 'current' //页码的参数名称，默认：page
            , limitName: 'size' //每页数据量的参数名，默认：limit
        }
        , cols: [[
            {
                field: 'name'
                , title: '商品名称'
                , align: 'center'
                , Width: 182
            }
            , {
                field: 'summary'
                , title: '商品摘要'
                , align: 'center'
                , width: 334
                , event: 'detail'
            }
            , {
                field: 'price'
                , title: '商品价格'
                , align: 'center'
                , Width: 146
            }
            , {
                field: 'saled'
                , title: '商品销量'
                , align: 'center'
                , Width: 136
            }
            , {
                field: 'stock'
                , title: '商品库存'
                , align: 'center'
                , Width: 105
            }
            , {
                field: 'isActive'
                , title: '是否参加活动'
                , align: 'center'
                , Width: 133
                , templet: (d) => {
                    return d.isActive == '1' ? "是" : "否"
                }
            }
            , {
                field: 'updateTime'
                , title: '删除时间'
                , align: 'center'
                , Width: 148
                , sort: true
                , templet: (d) => {
                    return Tool.formatDate(d.updateTime, 'yyyy/MM/dd');
                }
            }
            , {
                title: '操作'
                , width: 189
                , align: 'center'
                , fixed: 'right'
                , templet: (d) => {
                    let html = '<a class="layui-btn layui-btn-sm" lay-event="back">回退</a>' +
                        '<a class="layui-btn layui-btn-danger layui-btn-sm" lay-event="del">删除</a>';
                    return html;
                }
            }
        ]]
    });


    /**
     * 创建监听工具
     */
    table.on('tool(productRecycleTable)', function (obj) {
        var data = obj.data;
        if (obj.event == 'back') {
            layer.confirm('是否移出回收站?', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: "doOpeProduct",
                    type: "POST",
                    data: {'id': data.id, 'status': 'E'},
                    success: (res) => {
                        if (res.code === 200) {
                            layer.msg("操作成功", {icon: 6, time: 800});
                            setTimeout(() => {
                                layer.close(index);
                                table.reload('productRecycleTable', {page: {curr: 1}});
                            }, 800)
                        } else {
                            layer.msg(res.msg, {icon: 5, time: 500});
                        }
                    }
                });
            });
        } else if (obj.event == 'del') {
            layer.confirm('是否彻底删除?', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: "doOpeProduct",
                    type: "POST",
                    data: {'id': data.id, 'status': 'COMPLETE'},
                    success: (res) => {
                        if (res.code === 200) {
                            layer.msg("操作成功", {icon: 6, time: 800});
                            setTimeout(() => {
                                layer.close(index);
                                table.reload('productRecycleTable', {page: {curr: 1}});
                            }, 800)
                        } else {
                            layer.msg(res.msg, {icon: 5, time: 500});
                        }
                    }
                });
            });
        }
    });
});
