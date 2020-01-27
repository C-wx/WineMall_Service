<!DOCTYPE html>
<html lang="en">
<#assign base=request.contextPath />
<head>
    <meta charset="utf-8">
    <title>教学质量监控系统</title>
    <script src="${base}/js/jquery.min.js"></script>
    <script src="${base}/js/tool.js"></script>

    <!--layui-->
    <script src="${base}/plugins/layui/layui.js"></script>
    <link rel="stylesheet" href="${base}/plugins/layui/css/layui.css">
    <!-- Font Awesome CSS-->
    <link rel="stylesheet" href="${base}/plugins/font-awesome/css/font-awesome.min.css">

    <link rel="stylesheet" type="text/css" href="${base}/css/common.css"/>

    <script src="${base}/js/productManage.js"></script>
</head>
<script>
    $(function () {
        $(".menu-item").click(function () {
            $(".menu-item").removeClass("menu-item-active");
            $(this).addClass("menu-item-active");
        });
    })
</script>
<body>
<div id="wrap">
    <!-- 左侧菜单栏目块 -->
    <div class="leftmenu" id="leftmenu">
        <div id="logoDiv">
            <div>
                <i class="fa fa-user-secret" style="color: #a9a9a9;"></i>&nbsp;珑泉古酿
            </div>
        </div>
        <div class="menu-title">商品中心</div>
        <div class="menu-item menu-item-active">
            <a href="/toProductManage"><i class="fa fa-sitemap"></i>&nbsp;&nbsp;商品管理</a>
        </div>
        <div class="menu-item">
            <a href="/toProductRecycle"><i class="fa fa-trash"></i>&nbsp;&nbsp;商品回收站</a>
        </div>
        <div class="menu-title">订单中心</div>
        <div class="menu-item">
            <a href="/toOrderManage"><i class="fa fa-newspaper-o"></i>&nbsp;&nbsp;订单管理</a>
        </div>
        <div class="menu-title">评论中心</div>
        <div class="menu-item">
            <a href="/toCommentManage"><i class="fa fa-pencil-square-o"></i>&nbsp;&nbsp;评论管理</a>
        </div>
        <div class="menu-title">售后中心</div>
        <div class="menu-item">
            <a href="/toServiceManage"><i class="fa fa-warning"></i>&nbsp;&nbsp;售后管理</a>
        </div>
    </div>
    <!-- 右侧具体内容栏目 -->
    <div id="rightContent">
        <div class="check-div">
            <span>
                <i class="fa fa-user"></i>&nbsp;&nbsp;用户：商家1号&nbsp;&nbsp;&nbsp;&nbsp;
                <a href="/logout" class="fa fa-sign-out" style="cursor: pointer"></a>
            </span>
        </div>

        <div class="layui-fluid layui-anim layui-anim-scale" style="padding: 50px;">
            <div class="layui-row layui-form">
                <div class="layui-card" style="width: 1400px;">
                    <div class="layui-card-header">
                        <strong style="font-size: 22px;font-family: 'kaiti';letter-spacing: 2px">商品管理</strong>
                        <button class="layui-btn layui-btn-sm" style="margin-left: 1150px" type="button" onclick="doAdd()">添加商品</button>
                    </div>
                    <div class="layui-card-body" style="padding-top: 25px;">
                        <table id="productTable" lay-filter="productTable"></table>
                        <script type="text/html" id="isActive">
                            <input type="checkbox" name="isActive" value="{{d.id}}" title="是" lay-filter="isActive" {{
                                   d.isActive>0 ? 'checked' : '' }}>
                        </script>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>