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
    <script src="${base}/js/productOpe.js"></script>
    <title>珑泉古酿</title>
</head>
<body style="background-color: #f4f5f6">
<div class="container">
    <div class="layui-fluid layui-anim layui-anim-scale" style="padding: 20px;">
        <div class="layui-row">
            <div class="layui-card">
                <div class="layui-card-body" style="padding-top: 25px;">
                    <form enctype="multipart/form-data" method="post" class="layui-form">
                        <input type="hidden" name="id" value=<#if product??>"${product.id}"</#if>>
                        <div class="layui-form-item row">
                            <div class="layui-col-md2">
                                <label>商品名称：</label>
                            </div>
                            <div class="layui-col-md4">
                                <input type="text" name="name" autocomplete="off" class="layui-input" value=<#if product??>"${product.name!}"</#if>>
                            </div>
                        </div>
                        <div class="layui-form-item row">
                            <div class="layui-col-md2">
                                <label>商品图片：</label>
                            </div>
                            <div class="layui-col-md8">
                                <#if product?? && product.imageList?? && (product.imageList?size > 0) >
                                    <#list product.imageList as image>
                                        <div id ="d_${image_index}" style="float:left;margin-left:20px;" class="imageHover">
                                            <input id="file_${image_index}" type="file" name="files" style="display:none;" onChange="replace_image(${image_index})"/>
                                            <img id="image_${image_index}"  onclick="click_image(${image_index})" style="cursor:pointer;" src="${image.url}" height="100px" width="100px"/>
                                            <i class="fa fa-trash-o" style="display: none;font-size: 16px" onclick="delImage(${image.id})"></i>
                                        </div>
                                    </#list>
                                    <#if product.imageList?size < 3>
                                            <div id ="d_2" style="float:left;margin-left:20px;" class="imageHover">
                                                <input id="file_2" type="file" name="files" style="display:none;" onChange="replace_image(2)"/>
                                                <img id="image_2" onclick="click_image(2)" style="cursor:pointer;" src="/images/upload_hover.png" height="100px" width="100px"/>
                                            </div>
                                    </#if>
                                <#else >
                                    <div id ="d_0" style="float:left;margin-left:20px;" class="imageHover">
                                        <input id="file_0" type="file" name="files" style="display:none;" onChange="replace_image(0)"/>
                                        <img id="image_0" onclick="click_image(0)" style="cursor:pointer;" src="/images/upload_hover.png" height="100px" width="100px"/>
                                    </div>
                                </#if>
                            </div>
                        </div>
                        <div class="layui-form-item row">
                            <div class="layui-col-md2">
                                <label>商品摘要：</label>
                            </div>
                            <div class="layui-col-md8" style="width: 300px">
                                <textarea name="summary" rol="4" class="layui-textarea"><#if product??>${product.summary!}</#if></textarea>
                            </div>
                        </div>
                        <div class="layui-form-item row">
                            <div class="layui-col-md2">
                                <label>商品价格：</label>
                            </div>
                            <div class="layui-col-md4" style="width: 90px">
                                <input type="text" name="price" autocomplete="off" class="layui-input" value=<#if product??>"${product.price!}"</#if>>
                            </div>
                        </div>
                        <div class="layui-form-item row">
                            <div class="layui-col-md2">
                                <label>商品库存：</label>
                            </div>
                            <div class="layui-col-md2" style="width: 90px">
                                <input type="number" name="stock" autocomplete="off" class="layui-input" value=<#if product??>"${product.stock!}"</#if>>
                            </div>
                        </div>
                        <#if ope != 'Edit'>
                            <div class="layui-form-item row">
                                <div class="layui-col-md2">
                                    <label>品种：</label>
                                </div>
                                <div class="layui-col-md2" style="width: 150px">
                                    <input type="text" name="variety" autocomplete="off" class="layui-input" value=<#if product??>"${product.stock!}"</#if>>
                                </div>
                            </div>
                            <div class="layui-form-item row">
                                <div class="layui-col-md2">
                                    <label>类型：</label>
                                </div>
                                <div class="layui-col-md2" style="width: 150px">
                                    <input type="text" name="type" autocomplete="off" class="layui-input" value=<#if product??>"${product.stock!}"</#if>>
                                </div>
                            </div>
                            <div class="layui-form-item row">
                                <div class="layui-col-md2">
                                    <label>年份：</label>
                                </div>
                                <div class="layui-col-md2" style="width: 90px">
                                    <input type="text" name="years" autocomplete="off" class="layui-input" value=<#if product??>"${product.stock!}"</#if>>
                                </div>
                            </div>
                            <div class="layui-form-item row">
                                <div class="layui-col-md2">
                                    <label>产品规格：</label>
                                </div>
                                <div class="layui-col-md2" style="width: 90px">
                                    <input type="text" name="capacity" autocomplete="off" class="layui-input" value=<#if product??>"${product.stock!}"</#if>>
                                </div>
                            </div>
                            <div class="layui-form-item row">
                                <div class="layui-col-md2">
                                    <label>酒精度：</label>
                                </div>
                                <div class="layui-col-md2" style="width: 90px">
                                    <input type="text" name="degree" autocomplete="off" class="layui-input" value=<#if product??>"${product.stock!}"</#if>>
                                </div>
                            </div>
                        <#else>
                            <#list propertyList as property>
                                <div class="layui-form-item row">
                                    <div class="layui-col-md2">
                                        <label>${property.name}：</label>
                                    </div>
                                    <div class="layui-col-md2" style="width: 120px">
                                        <input type="text" name="degree" autocomplete="off" class="layui-input" value="${property.value!}">
                                    </div>
                                </div>
                            </#list>
                        </#if>

                        <div class="layui-form-item row">
                            <div class="layui-col-md2">
                                <label>商品介绍：</label>
                            </div>
                            <div class="layui-col-md8" style="width: 300px">
                                <textarea name="introduction" rol="4" class="layui-textarea"><#if product??>${product.introduction!}</#if></textarea>
                            </div>
                        </div>
                        <div class="layui-form-item row">
                            <div class="layui-col-md2">
                                <label>是否参加活动：</label>
                            </div>
                            <div class="layui-col-md4">
                                <input type="checkbox" name="isActive" lay-skin="switch" lay-text="是|否" <#if product?? && product.isActive =='1'>checked</#if>>
                            </div>
                        </div>
                        <div class="layui-form-item" style="margin-left: 350px">
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
        width: 150px;
        font-size: 16px;
    }
</style>
<script>
    layui.use(['form', 'layedit', 'laydate'], function () {
        var form = layui.form ;
        form.render();
    });
</script>
</html>