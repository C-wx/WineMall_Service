layui.use(['form', 'table'], function () {
    var form = layui.form,
        table = layui.table;
    form.render();

    /**
     * 初始化表格
     */
    var orderTable = table.render({
        elem: '#orderTable'
        , url: '/orderPage'
        , page: true
        , limit: 10
        , height: 'full'
        , method: 'get'
        , request: {
            pageName: 'current' //页码的参数名称，默认：page
            , limitName: 'size' //每页数据量的参数名，默认：limit
        }
        , cols: [[
            {title: '序号', align: 'center', templet: '#xuhao'},
            {
                field: 'orderCode'
                , title: '订单编码'
                , align: 'center'
                , width: 334
            }
            , {
                field: 'status'
                , title: '订单状态'
                , align: 'center'
                , Width: 136
                , templet: (d) => {
                    console.log(d)
                    let status;
                    switch (d.status) {
                        case "WP":
                            status = "待付款";
                            break;
                        case "WD":
                            status = "待发货";
                            break;
                        case "WC":
                            status = "待收货";
                            break;
                        case "WR":
                            status = "待评价";
                            break;
                        case "YR":
                            status = "已评价";
                            break;
                        case "DD":
                            status = "取消订单";
                        case "DW":
                            status = "退款处理中";
                        case "DWTK":
                            status = "等待退款";
                        case "DYTK":
                            status = "已退款";
                        case "DS":
                            status = "同意退货";
                        case "DF":
                            status = "拒绝退货";
                            break;
                    }
                    return status;
                }
            }
            , {
                field: 'payTime'
                , title: '付款时间'
                , align: 'center'
                , Width: 148
                , sort: true
                , templet: (d) => {
                    if (d.payTime != null) {
                        return Tool.formatDate(d.payTime, 'yyyy/MM/dd');
                    } else {
                        return "";
                    }
                }
            }
            , {
                title: '操作'
                , width: 189
                , align: 'center'
                , fixed: 'right'
                , templet: (d) => {
                    let deliveryHtml = '';
                    let detailHtml = '<a class="layui-btn layui-bg-cyan layui-btn-sm" lay-event="detail">详情</a>';
                    if (d.status == 'WD') {
                        deliveryHtml += '<a class="layui-btn layui-bg-orange layui-btn-sm" lay-event="delivery">发货</a>';
                    } else if (d.status != 'DD' & d.status != 'WP') {
                        deliveryHtml += '<a class="layui-btn layui-btn-disabled layui-btn-sm">已发货</a>';
                    }
                    return detailHtml + deliveryHtml;
                }
            }
        ]]
    });


    /**
     * 创建监听工具
     */
    table.on('tool(orderTable)', function (obj) {
        var data = obj.data;
        if (obj.event == 'detail') {         //点击查看内容详情
            layer.open({
                type: 2,
                title: '',
                area: ['700px', '700px'],
                offset: 'auto',
                content: '/toOrderDetail?orderCode=' + data.orderCode,
                shade: 0.4
            })
        } else if (obj.event == 'delivery') {
            layer.open({
                type: 2,
                title: '',
                area: ['450px', '290px'],
                offset: 'auto',
                content: '/toOrderDelivery?orderCode=' + data.orderCode,
                shade: 0.4
            })
        } else if (obj.event == 'doEdit') {
            layer.open({
                type: 2,
                title: '',
                area: ['650px', '600px'],
                offset: 'auto',
                content: '/toOpeProduct?id=' + data.id,
                shade: 0.4
            })
        }
    });

    /**
     * 监听搜索事件
     */
    $('button[data-type]').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
    var active = {
        keyLike: function () {                          //关键词模糊搜索
            const orderCode = $('#orderCode');
            //执行重载
            table.reload('orderTable', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                , where: {
                    orderCode: orderCode.val()
                }
            });
        },
        reload: function () {                           //重置加载页面
            $('#orderCode').val("");
            table.reload('orderTable', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                , where: {
                    orderCode: $('#orderCode').val()
                }
            });
        }
    };

    /*
     * 监听"是否参加活动"开关
     */
    form.on('checkbox(isActive)', function (obj) {
        const objId = this.value;
        let isActive;
        if (obj.elem.checked) {
            isActive = '1';
        } else {
            isActive = '0';
        }
        $.ajax({
            url: "doOpeProduct",
            type: "POST",
            data: {'id': objId, 'isActive': isActive},
            success: (result) => {
                if (result.code == 200) {
                    layer.msg("操作成功", {time: 800});
                } else {
                    layer.msg(result.msg);
                }
            }
        });
    });

    /**
     * 添加商品
     */
    doAdd = () => {
        layer.open({
            type: 2,
            title: '',
            area: ['650px', '600px'],
            offset: 'auto',
            content: '/toOpeProduct?',
            shade: 0.4
        })
    }
});
