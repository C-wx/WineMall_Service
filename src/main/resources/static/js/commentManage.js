layui.use(['form', 'table'], function () {
    var form = layui.form,
        table = layui.table;
    form.render();

    /**
     * 初始化表格
     */
    var commentTable = table.render({
        elem: '#commentTable'
        , url: '/commentPage'
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
                field: 'productName'
                , title: '商品名称'
                , align: 'center'
                , Width: 182
                , templet: (d) => {
                    return d.product.name;
                }
            }
            , {
                field: 'orderCode'
                , title: '订单编码'
                , align: 'center'
                , width: 334
                , templet: (d) => {
                    return d.orderCode;
                }
            }
            , {
                field: 'content'
                , title: '评价内容'
                , align: 'center'
                , event: 'detail'
                , width: 334
            }
            , {
                field: 'createTime'
                , title: '评论时间'
                , align: 'center'
                , Width: 148
                , sort: true
                , templet: (d) => {
                    return Tool.formatDate(d.createTime, 'yyyy/MM/dd');
                }
            }
            , {
                title: '操作'
                , width: 189
                , align: 'center'
                , fixed: 'right'
                , templet: (d) => {
                    let html = '<a class="layui-btn layui-bg-red layui-btn-sm" lay-event="delete">删除</a>';
                    return html;
                }
            }
        ]]
    });


    /**
     * 创建监听工具
     */
    table.on('tool(commentTable)', function (obj) {
        var data = obj.data;
        if (obj.event == 'detail') {         //点击查看内容详情
            layer.open({
                type: 0,
                title: '内容详情',
                btn: [],
                offset: 'auto',
                content: '<div style="padding: 20px;">' + data.content + '</div>',
                shade: 0.4
            })
        } else if (obj.event == 'delete') {
            var str = "<div>确认删除该评论吗？</div>";
            layer.confirm(str, {btn: ['确定', '取消'], title: "提示"}, function () {
                $.ajax({
                    url: "/doOpeComment",
                    data: {
                        id: data.id,
                        status: "D"
                    }
                })
            });
        }
    });
});
