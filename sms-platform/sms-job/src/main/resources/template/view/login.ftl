<#assign ctx=request.getContextPath()>
<!doctype html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>登陆</title>
    <link rel="stylesheet" type="text/css" href="${ctx }/css/admin_login.css"/>
</head>
<body>
<div class="admin_login_wrap">
    <h1>后台管理</h1>
    <div class="adming_login_border">
        <div class="admin_input">
            <form id="addForm" action="${ctx }/signin" method="post" onsubmit="signin()">
                <ul class="admin_items">
                    <li>
                        <label for="user">用户名：</label>
                        <input type="text" name="username" value="${username }" id="user" size="35" placeholder="请输入用户名" required="required"  class="admin_input_style"/>
                    </li>
                    <li>
                        <label for="pwd">密码：</label>
                        <input type="password" name="password"value="${password }"  placeholder="请输入密码" required="required" id="password" size="35"
                               class="admin_input_style"/>
                    </li>
                    <li>
                        <input type="submit" tabindex="3" id="submitLogin" value="登陆" class="btn btn-primary"/>
                    </li>
                </ul>
            </form>
        </div>
    </div>
    <p class="admin_copyright" style="color: red">&nbsp;${error }</p>
    <p class="admin_copyright"> &copy; 2018 Powered by 辉隆集团</p>
</div>
</body>
<script type="text/javascript" src="${ctx }/js/jquery.min.1.91.js"></script>
</html>
