<#assign ctx=request.getContextPath()>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta name="format-detection" content="telephone=no">
    <title>手机号绑定</title>
    <link type="text/css" href="${ctx }/css/reset.css" rel="stylesheet">
    <link type="text/css" href="${ctx }/css/style.css" rel="stylesheet">
    <script type="text/javascript" src="${ctx }/js/jquery.min.1.91.js"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0,user-scalable=no,maximum-scale=1.0">
    <meta name="viewport" content="minimal-ui">
    <meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimal-ui">
</head>
<body>
<div class="hj_wrapPage">
    <div class="hj_wrapImgList">
        <center><p>&nbsp;</p></center>
        <center><p>绑定手机号</p></center>
        <center><p>&nbsp;</p></center>
    </div>
</div>
<div class="bd1">
    <div class="wa">
        <div class="wa_wrapImgList">
            <p style="color: red">&nbsp;</p>
            <p>您已经绑定手机号：</p>
            <p>&nbsp;</p>
            <p style="color: red" id="errormsg">${bindinfo.phone}</p>
        </div>
    </div>

        <div class="hj_wrapImgList4">
            <center>
            <#--<a href="login.html">提交</a><br/>-->
                <input type="button" value="换绑" onclick="changeBind()"
                       style="background: #009dd9;font-size: 2rem;font-weight: bold;padding-left:27%; padding-right:27%;color: #FFFFFF">
            </center>
        </div>
</div>
</div>
<div style="text-align:center;margin:50px 0; font:normal 14px/24px 'MicroSoft YaHei';">
    <p>
    <#--openid = ${userinfo.openid}-->
    <#--nickname = ${userinfo.nickname}-->
    <#--sex = ${userinfo.sex}-->
    <#--province = ${userinfo.province}-->
    <#--city = ${userinfo.city}-->
    <#--country = ${userinfo.country}-->
    <#--headimgurl = ${userinfo.headimgurl}-->
    <#--private String[] privilege;-->
    <#--unionid = <#if userinfo?? && userinfo.unionid??>${userinfo.unionid}</#if>-->
    </p>
    <#--<p>绑定手机号后，NC消息以及业务消息将会通过公众号直接发送给您。（此处绑定的手机号必须与您在NC系统中的联系方式一致）</p>-->
    <#--<p>来源：<a href="http://sc.chinaz.com/" target="_blank">站长素材</a></p>-->
</div>
</body>
<script type="text/javascript">

    function changeBind() {
        window.location.href = '${ctx }/wx/showBind?type=change'
    }

</script>
</html>