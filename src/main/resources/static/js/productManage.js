layui.use(['form', 'table'], function () {
    var form = layui.form,
        table = layui.table;
    form.render();

    /**
     * 初始化表格
     */
    var productTable = table.render({
        elem: '#productTable'
        , url: '/productPage'
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
                , templet: '#isActive'
            }
            , {
                field: 'createTime'
                , title: '录入时间'
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
                    let auditHtml = '<a class="layui-btn layui-bg-lightsteelblue layui-btn-sm" lay-event="doEdit">编辑</a>';
                    let ableHtml = '<a class="layui-btn layui-btn-danger layui-btn-sm" lay-event="disable">删除</a>';
                    return auditHtml + ableHtml;
                }
            }
        ]]
    });


    /**
     * 创建监听工具
     */
    table.on('tool(productTable)', function (obj) {
        var data = obj.data;
        if (obj.event == 'detail') {         //点击查看内容详情
            layer.open({
                type: 0
                , btn: []
                , title: '内容详情'
                , area: ['200px', '200px']
                , offset: 'auto'
                , shadeClose: true
                , id: 'layerDemo' + data.id
                , content: '<div style="padding: 20px;"> <div>' + data.summary + '</div> </div>'
                , shade: 0.3
                , anim: 5
            });
        } else if (obj.event == 'disable') {
            layer.confirm('是否放入回收站?', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: "doOpeProduct",
                    type: "POST",
                    data: {'id': data.id, 'status': 'D'},
                    success: (res) => {
                        if (res.code === 200) {
                            layer.msg("操作成功", {icon: 6, time: 800});
                            setTimeout(() => {
                                layer.close(index);
                                table.reload('productTable', {page: {curr: 1}});
                            }, 800)
                        } else {
                            layer.msg(res.msg, {icon: 5, time: 500});
                        }
                    }
                });
            });
        }else if (obj.event == 'doEdit') {
            layer.open({
                type: 2,
                title: '',
                area: ['600px', '960px'],
                offset: 'auto',
                content: '/toOpeProduct?id='+data.id,
                shade:0.4
            })
        }
    });

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
     * 监听搜索事件
     */
    $('button[data-type]').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
    var active = {
        keyLike: function () {                          //关键词模糊搜索
            const name = $('#name');
            //执行重载
            table.reload('productTable', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                , where: {
                    name: name.val()
                }
            });
        },
        reload: function () {                           //重置加载页面
            $('#name').val("");
            table.reload('productTable', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                , where: {
                    name: $('#name').val()
                }
            });
        }
    };

    /**
     * 添加商品
     */
    doAdd = ()=>{
        layer.open({
            type: 2,
            title: '',
            area: ['600px', '960px'],
            offset: 'auto',
            content: '/toOpeProduct?',
            shade:0.4
        })
    }
});
