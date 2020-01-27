<!DOCTYPE html>
<html lang="en">
<#assign base=request.contextPath />
<head>
    <script src="${base}/js/jquery.min.js"></script>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <link rel="stylesheet" href="${base}/css/login.css">
    <!--layui-->
    <script src="${base}/plugins/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${base}/plugins/layui/css/layui.css">
    <!-- Font Awesome CSS-->
    <link rel="stylesheet" href="${base}/plugins/font-awesome/css/font-awesome.min.css">
    <title>珑泉古酿</title>
</head>
<body style="background-color: #f4f5f6">
<div class="container">
    <div class="layui-fluid layui-anim layui-anim-scale" style="padding: 20px;">
        <div class="layui-row">
            <div class="layui-card">
                <div class="layui-card-body" style="padding-top: 25px;">
                    <div class="content">
                        <span class="title">商品名称：</span>
                        <span class="value">${order.product.name!}</span>
                    </div>
                    <div class="content">
                        <span class="title">订单编码：</span>
                        <span class="value">${order.orderCode!}</span>
                    </div>
                    <div class="content">
                        <span class="title">购买数量：</span>
                        <span class="value">${order.num!}</span>
                    </div>
                    <div class="content">
                        <span class="title">订单总价：</span>
                        <span class="value">${order.price!}￥</span>
                    </div>
                    <div class="content">
                        <span class="title">收货人：</span>
                        <span class="value">${order.receiveName!}</span>
                    </div>
                    <div class="content">
                        <span class="title">联系电话：</span>
                        <span class="value">${order.phone!}</span>
                    </div>
                    <div class="content">
                        <span class="title">收货地址：</span>
                        <span class="value">${order.addr!}</span>
                    </div>
                    <div class="content">
                        <span class="title">邮政编码：</span>
                        <span class="value">${order.postCode!}</span>
                    </div>
                    <div class="content">
                        <span class="title">订单留言：</span>
                        <span class="value">${order.comment!}</span>
                    </div>
                    <div class="content">
                        <span class="title">付款时间：</span>
                        <span class="value">${order.createTime?string('yyyy年MM月dd日')}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<style>
    .content{
        margin-bottom: 20px;
    }
    .title{
        font-size: 20px;
        color: #43a6b6;
    }
    .value{
        font-size: 20px;
        font-weight: 800;
    }
</style>
</html>