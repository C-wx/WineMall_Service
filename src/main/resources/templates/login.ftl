<!DOCTYPE html>
<html lang="en">
<#assign base=request.contextPath />
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <!-- Loding font -->
    <link href="https://fonts.googleapis.com/css?family=Montserrat:300,700" rel="stylesheet">
    <!--login-->
    <link rel="stylesheet" href="${base}/css/login.css">
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
                <form>
                    <div class="form-group">
                        <input type="text" class="form-control" id="name" aria-describedby="emailHelp" placeholder="Enter email">
                    </div>
                    <br>
                    <div class="form-group">
                        <input type="password" class="form-control" id="exampleInputPassword1" placeholder="Password">
                    </div>
                    <br>
                    <div class="form-group">
                        <input type="password" class="form-control" id="exampleInputPassword1" placeholder="Password">
                    </div>
                    <br>
                    <button type="submit" class="btn btn-lg btn-block btn-success">Sign in</button>
                </form>
        </div>
    </div>
</div>
</body>
</html>