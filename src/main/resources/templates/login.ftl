<!DOCTYPE html>
<html lang="en">
<#assign base=request.contextPath />
<head>
    <script src="${base}/js/jquery.min.js"></script>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <link href="https://fonts.googleapis.com/css?family=Montserrat:300,700" rel="stylesheet">
    <link rel="stylesheet" href="${base}/css/login.css">
    <script src="${base}/js/login.js"></script>

    <!--layui-->
    <script src="${base}/plugins/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${base}/plugins/layui/css/layui.css">
    <!-- Font Awesome CSS-->
    <link rel="stylesheet" href="${base}/plugins/font-awesome/css/font-awesome.min.css">
    <title>珑泉古酿</title>
</head>
<body>
<div id="login-bg" class="container-fluid">
    <div class="bg-img"></div>
    <div class="bg-color"></div>
</div>
<div class="container" id="login">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="login">
                <h2>珑泉古酿商家端</h2>
                <form id="login_form">
                    <div class="form-group">
                        <input type="text" placeholder="账号" id="account" name="account" class="form-control"/>
                    </div>
                    <br>
                    <div class="form-group">
                        <input type="password" placeholder="密码" id="pwd" name="pwd" class="form-control"/>
                    </div>
                    <br>
                    <div class="form-group row">
                        <div class="col-lg-8">
                            <input type="text" placeholder="验证码" id="verifyCode" name="verifyCode" class="form-control"/>
                        </div>
                        <div class="col-lg-4">
                            <img src="/verycode/getImgCode" id="imgObj" onclick="changeImg()">
                        </div>
                    </div>
                    <br>
                    <button type="button" class="btn btn-lg btn-block btn-success" id="login_btn">登录</button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    function changeImg() {
        var imgSrc = $("#imgObj");
        var src = imgSrc.attr("src");
        imgSrc.attr("src", chgUrl(src));
    }
    function chgUrl(url) {
        var timestamp = (new Date()).valueOf();
        urlurl = url.substring(0, 17);
        if ((url.indexOf("&") >= 0)) {
            urlurl = url + "×tamp=" + timestamp;
        } else {
            urlurl = url + "?timestamp=" + timestamp;
        }
        urlurl = "${base}/verycode/getImgCode?timestamp=" + timestamp + "&imgCodeType=NUM";
        return urlurl;
    }
</script>
</html>