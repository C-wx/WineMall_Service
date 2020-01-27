$(() => {
    $("body").on("click", "#login_btn", () => {
        var account = $("#account").val();
        var pwd = $("#pwd").val();
        var verifyCode = $("#verifyCode").val();
        if (!account) {
            layer.msg("请输入账号！", {icon: 5, time: 800, anim: 6})
        } else if (!pwd) {
            layer.msg("请输入密码！", {icon: 5, time: 800, anim: 6})
        } else if (!verifyCode) {
            layer.msg("请输入验证码！", {icon: 5, time: 800, anim: 6})
        }
        $.ajax({
            url: "doLogin",
            type: "GET",
            data: $("#login_form").serialize(),
            success: (e) => {
                if (e.code == 200) {
                    layer.msg("登录成功", {icon: 6, time: 800, anim: 1});
                    setTimeout(() => {
                        location.href = "/toIndex";
                    }, 1000);
                } else {
                    layer.msg(e.msg, {icon: 5, time: 800, anim: 1});
                    setTimeout(function () {
                        $("#verifyCode").val("");
                        changeImg();
                    }, 1000);
                }
            }
        });
    });
})
$(document).keyup(function (event) {
    if (event.keyCode == 13) {
        $("#login_btn").click();
    }
});