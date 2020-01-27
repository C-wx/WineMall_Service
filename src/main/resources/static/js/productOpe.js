layui.use(['form', 'layedit', 'laydate'], function () {
    var form = layui.form;
    form.render();
    form.on('submit(submit)', function (data) {
        $.ajax({
            url: "/doOpeProduct",
            type: "POST",
            data: new FormData($("form")[0]),
            processData: false,
            contentType: false,
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

function click_image(index) {
    $("#file_" + index).click();
}

function replace_image(index) {
    // 获得图片对象
    var blob_image = $("#file_" + index)[0].files[0];
    var url = window.URL.createObjectURL(blob_image);
    // 替换image
    $("#image_" + index).attr("src", url);
    var length = $(":file").length;
    console.log($("#file_" + index).val());
    if ((index + 1) == length && length < 3) {
        // 用户选择的是最后一张图片
        add_image(index);
    }
}

function add_image(index) {
    var a = '<div id ="d_' + (index + 1) + '" style="float:left;margin-left:20px;" class="imageHover">';
    var b = '<input id="file_' + (index + 1) + '" type="file" name="files" style="display:none;" onChange="replace_image(' + (index + 1) + ')"/>'
    var c = '<img id="image_' + (index + 1) + '" onclick="click_image(' + (index + 1) + ')" style="cursor:pointer;" src="/images/upload_hover.png" width="100px" height="100px"/>'
    var d = '</div>';
    $("#d_" + index).after(a + b + c + d);
}

$(() => {
    $(".imageHover").mouseenter(function () {
        $(this).children("i").show();
    });
    $(".imageHover").mouseleave(function () {
        $(this).children("i").hide();
    });
});
delImage = (id) => {
    $.ajax({
        url: "/doDelImage",
        type: "POST",
        data: {'id': id},
        success: function (res) {
            if (res.code == 200) {
                layer.msg("操作成功", {icon: 6, time: 800});
                setTimeout(() => {
                    location.reload();
                }, 800);
            } else {
                layer.msg(res.msg, {icon: 5, time: 800});
            }
        }
    });
};