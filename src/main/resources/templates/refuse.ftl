<!DOCTYPE html>
<html lang="en">
<#assign base=request.contextPath />
<head>
    <script src="${base}/js/jquery.min.js"></script>
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
                    <form class="layui-form">
                        <input type="hidden" name="id" value=${id!}>
                        <input type="hidden" name="orderCode" value=${orderCode!}>
                        <input type="hidden" name="status" value="F">
                        <div class="layui-form-item row">
                            <div class="layui-col-md2">
                                <label>拒绝原因：</label>
                            </div>
                            <div class="layui-col-md4" style="width: 240px">
                                <textarea name="refuse" class="layui-textarea"></textarea>
                            </div>
                        </div>
                        <div class="layui-form-item" style="margin-left: 290px">
                            <button class="layui-btn" lay-submit="" lay-filter="submit" type="button">提交</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<style>
    label {
        margin-left: 10px;
        width: 110px;
        font-size: 16px;
    }
</style>
<script>
    layui.use(['form'], function () {
        var form = layui.form;
        form.render();

        form.on('submit(submit)', function (data) {
            $.ajax({
                url: "/doOpeService",
                type: "POST",
                data: data.field,
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
            return false;
        });
    });
</script>
</html>