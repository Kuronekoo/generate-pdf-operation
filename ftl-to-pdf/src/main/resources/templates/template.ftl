<!DOCTYPE html>
<html lang="en">
<#assign basePath="http://localhost:8080/templates">
<head>
    <meta charset="UTF-8"/>
    <title></title>
    <link href="${basePath}/css/css.css" rel="stylesheet" type="text/css"/>
    <style>
        @page {
            size: 297mm 420mm; /*设置纸张大小:A4(210mm 297mm)、A3(297mm 420mm) 横向则反过来*/
            margin: 0.25in;
            padding: 1em;
            @top-center { content: element(header) };
            @bottom-right{
                content:"第" counter(page) "页  共 " counter(pages) "页";
                font-family: SimSun;
                font-size: 12px;
                color:#000;
            };
        }
    </style>
</head>
<body>
<div class="header"  style="font-family: SimSun">
    <img  class="image1" src="${basePath}/image/neko.png"/>
    <img class="image2" src="${basePath}/image/neko.png"/>
       <h3 class="header-title" >欧拉欧拉欧拉欧拉</h3>
    <div class="p">
        <p class="title-date-form"> 日期：${year}年${month}月${day}日</p>
    </div>
</div>
<div class="main-body" style="font-family: SimSun">
    <table class="table" cellpadding="0" cellspacing="0">
        <tr class="tr">
            <td class="td">文件</td>
            <td ></td>
            <td class="td3">主责部室</td>
            <td></td>
        </tr>
        <tr>
            <td class="td" rowspan="3">经办人
                 意　见
            </td>
            <td class="examine" rowspan="3"><h3>呈xxxxx、XXXXX审批。</h3></td>
            <td>拟稿人</td>
            <td></td>
        </tr>
        <tr>
            <td>核稿人</td>
            <td></td>
        </tr>
        <tr>
            <td>页数</td>
            <td></td>
        </tr>
        <tr class="tr1">
            <td class="td">
                负责人审批
            </td>
            <td colspan="3" >
                <div > ${idea}</div>
                <div class="td55"> ${year}年${month}月${day}日</div>
            </td>
        </tr>
        <tr class="tr1">
            <td class="td">
                审　批
            </td>
            <td colspan="3" >
                <div > ${idea}</div>
                <div class="td5"> ${year}年${month}月${day}日</div>
            </td>
        </tr>
        <tr>
            <td class="td">主送部门</td>
            <td colspan="3"></td>
        </tr>
        <tr>
            <td class="td">抄送部门</td>
            <td colspan="3"></td>
        </tr>
        <tr>
            <td class="td">备注</td>
            <td colspan="3"></td>
        </tr>
    </table>
</div>
</body>
</html>