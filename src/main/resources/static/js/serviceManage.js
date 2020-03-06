layui.use(['form', 'table'], function () {
    var form = layui.form,
        table = layui.table;
    form.render();

    /**
     * 初始化表格
     */
    var serviceTable = table.render({
        elem: '#serviceTable'
        , url: '/servicePage'
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
                field: 'orderCode'
                , title: '订单编码'
                , align: 'center'
                , width: 234
                , templet: (d) => {
                    return d.orderCode;
                }
            }
            , {
                field: 'reason'
                , title: '退款原因'
                , align: 'center'
                , event: 'detail'
                , width: 278
            }
            , {
                field: 'createTime'
                , title: '退货时间'
                , align: 'center'
                , Width: 198
                , sort: true
                , templet: (d) => {
                    console.log(d)
                    return Tool.formatDate(d.createTime, 'yyyy/MM/dd HH:mm:ss');
                }
            }
            , {
                field: 'trackCode'
                , title: '寄回单号'
                , align: 'center'
                , event: 'detail'
                , width: 263
            }
            , {
                title: '操作'
                , width: 192
                , align: 'center'
                , fixed: 'right'
                , templet: (d) => {
                    let html = "";
                    switch (d.status) {
                        case "W":
                            html += '<a class="layui-btn layui-btn-success layui-btn-sm" lay-event="agree">同意</a>'
                                + '<a class="layui-btn layui-btn-danger layui-btn-sm" lay-event="refuse">拒绝</a>';
                            break;
                        case "S":
                            html += '已同意';
                            html += '&nbsp;&nbsp;&nbsp;<a class="layui-btn layui-btn-success layui-btn-sm" lay-event="tuikuan">退款</a>';
                            break;
                        case "F":
                            html += '已拒绝';
                            break;
                        case "SS":
                            html += '退款到账';
                            break;
                    }
                    return html;
                }
            }
        ]]
    });


    /**
     * 创建监听工具
     */
    table.on('tool(serviceTable)', function (obj) {
        var data = obj.data;
        if (obj.event == 'detail') {         //点击查看内容详情
            layer.open({
                type: 0,
                title: '内容详情',
                btn: [],
                offset: 'auto',
                content: '<div style="padding: 20px;">' + data.reason + '</div>',
                shade: 0.4
            })
        } else if (obj.event == 'refuse') {
            console.log(data)
            layer.open({
                type: 2,
                title: '',
                area: ['450px', '260px'],
                offset: 'auto',
                content: '/toRefuse?id=' + data.id + '&orderCode=' + data.orderCode,
                shade: 0.4
            })
        } else if (obj.event == 'agree') {
            layer.confirm('是否同意该请求?', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: "/doOpeService",
                    type: "POST",
                    data: {
                        "id": data.id,
                        "orderCode": data.orderCode,
                        "status": "S"
                    },
                    success: function (res) {
                        if (res.code == 200) {
                            layer.msg("操作成功", {icon: 6, time: 800});
                            setTimeout(() => {
                                var index = parent.layer.getFrameIndex(window.name);
                                parent.layer.close(index);
                                parent.location.reload();
                            }, 800);
                        } else {
                            layer.msg(res.msg, {icon: 5, time: 800});
                        }
                    }
                });
                layer.close(index);
            });
        } else if (obj.event == 'tuikuan') {
            layer.confirm('是否同意该请求?', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: "/doOpeService",
                    type: "POST",
                    data: {
                        "id": data.id,
                        "orderCode": data.orderCode,
                        "status": "SS"
                    },
                    success: function (res) {
                        if (res.code == 200) {
                            layer.msg("操作成功", {icon: 6, time: 800});
                            setTimeout(() => {
                                var index = parent.layer.getFrameIndex(window.name);
                                parent.layer.close(index);
                                parent.location.reload();
                            }, 800);
                        } else {
                            layer.msg(res.msg, {icon: 5, time: 800});
                        }
                    }
                });
                layer.close(index);
            });
        }
    });
});
