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
            <p>绑定手机号后，NC消息以及业务消息将会通过公众号直接发送给您。（此处绑定的手机号必须与您在NC系统中的联系方式一致）</p>
            <p style="color: red">&nbsp;</p>
            <p style="color: red" id="errormsg">&nbsp;</p>
        </div>
    </div>
    <form onsubmit="return bind();">
        <div class="hj_wrapImgList2">
            <center>
                <input type="text" name="phone" id="phone" placeholder="请输入要绑定的手机号码" required="required"
                       class="ku"/><br/>
                <input type="text" name="verifyCode" id="verifyCode" placeholder="图片验证码" required="required" class="ku"
                       style="width: 22%;vertical-align: middle">
                <img alt="验证码" onclick="this.src='${ctx}/defaultKaptcha?d='+new Date()*1" src="/defaultKaptcha"
                     style="width:20%;vertical-align: middle;margin-bottom: 15px"/>
                <input type="button" id="send" value="获取短信" onclick="sendMsg()"
                       style="background: #009dd9;font-size: 1rem;font-weight: bold;padding: 8px; color: #FFFFFF;vertical-align: middle;margin-bottom: 15px;width: 17%"/>
            </center>

            <center>
                <input type="text" name="smsCode" id="smsCode" placeholder="请输入收到的短信验证码" class="ku"
                       required="required"/>

                <#if userinfo?? && userinfo.openid??>
                    <input type="hidden" name="openid" id="openid" value="${userinfo.openid}"/>
                </#if>
                <#if userinfo?? && userinfo.nickname??>
                    <input type="hidden" name="nickname" id="nickname" value="${userinfo.nickname}"/>
                </#if>
            </center>
        </div>

        <div class="hj_wrapImgList4">
            <center>
            <#--<a href="login.html">提交</a><br/>-->
                <input type="submit" value="提交"
                       style="background: #009dd9;font-size: 2rem;font-weight: bold;padding-left:27%; padding-right:27%;color: #FFFFFF">
            </center>
        </div>
    </form>
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
    var countdown = 6;

    function sendMsg() {
        var phone = $('#phone').val();
        if (!isPhoneAvailable(phone)) {
            $('#errormsg').html("手机号格式错误");
            return false;
        }
        var verifyCode = $('#verifyCode').val();
        if (!isCodeAvailable(verifyCode, 4)) {
            $('#errormsg').html("图片验证码格式错误");
            return false;
        }
        $('#errormsg').html("");
        var targetUrl = "${ctx}/sendSMS";
        var data = {"verifyCode": verifyCode, "phone": phone};
        $.ajax({
            type: 'post',
            url: targetUrl,
            cache: false,
            data: data,
            dataType: 'json',
            success: function (data) {
                if (data.res == true) {
                    settime($('#send'));
                }
                else if (data.res == false) {
                    $('#errormsg').html('发送失败：' + data.msg);
                    alert('发送失败：' + data.msg);
                }
            },
            error: function () {
                alert("请求失败")
            }
        })
    }

    function bind() {
        var targetUrl = '${ctx }/bindPhone';
        var smsCode = $("#smsCode").val();
        var openid = $("#openid").val();
        var nickname = $("#nickname").val();
        var phone = $("#phone").val();
        var data = {"smsCode": smsCode, "phone": phone, "openid": openid, "nickname": nickname};
        $.ajax({
            type: 'post',
            url: targetUrl,
            cache: false,
            data: data,
            dataType: 'json',
            success: function (data) {
                if (data.res == true) {
                    window.location.href = '${ctx }/bindRes?res=true'
                    return false;
                }
                else if (data.res == false) {
                    $('#errormsg').html(data.msg);
                    return false;
                }
            },
            error: function () {
                alert("请求失败");
                return false
            }
        })


        return false;

    }

    function settime(obj) {
        if (countdown == 0) {
            obj.removeAttr("disabled");
            obj.val("获取短信");
            countdown = 6;
            return;
        } else {
            obj.attr("disabled", true);
            obj.val(countdown);
            countdown--;
        }
        setTimeout(function () {
            settime(obj)
        }, 1000)
    }

    function isPhoneAvailable(str) {
        var myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
        return myreg.test(str);
    }

    function isCodeAvailable(str, cnt) {
        var patrn = new RegExp("^\\d{" + cnt + "}$");
        return patrn.test(str);
    }
</script>
</html>