<!DOCTYPE html>
<html lang="en">
<#assign base=request.contextPath />
<head>
    <script src="${base}/js/jquery.min.js"></script>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
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
            <#list orderList as order>
                <div class="layui-card">
                    <div class="layui-card-body" style="padding-top: 25px;">
                        <div class="content">
                            <span class="title">商品名称：</span>
                            <span class="value">${order.product.name!}</span>
                        </div>
                        <div class="content">
                            <span class="title">购买数量：</span>
                            <span class="value">${order.num!}</span>
                        </div>
                        <div class="content">
                            <span class="title">订单总价：</span>
                            <span class="value" style="color:red">${order.price!}￥</span>
                        </div>
                    </div>
                </div>
            </#list>

            <div class="info">
                <span class="key">收货人：</span>
                <span class="value">${orderList[0].receiveName!}</span>
            </div>
            <div class="info">
                <span class="key">联系电话：</span>
                <span class="value">${orderList[0].phone!}</span>
            </div>
            <div class="info">
                <span class="key">收货地址：</span>
                <span class="value">${orderList[0].addr!}</span>
            </div>
            <div class="info">
                <span class="key">邮政编码：</span>
                <span class="value">${orderList[0].postCode!}</span>
            </div>
            <div class="info">
                <span class="key">订单留言：</span>
                <span class="value">${orderList[0].comment!}</span>
            </div>
            <div class="info">
                <span class="key">付款时间：</span>
                <span class="value">${orderList[0].createTime?string('yyyy年MM月dd日')}</span>
            </div>
        </div>
    </div>
</div>
<style>
    .content {
        margin-bottom: 20px;
    }

    .title {
        font-size: 20px;
        color: #43a6b6;
    }

    .value {
        font-size: 20px;
        font-weight: 800;
    }

    .key {
        font-size: 16px;
        font-weight: 800;
        margin-bottom: 10px;
    }

</style>
</html>