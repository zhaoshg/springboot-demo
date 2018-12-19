<#assign FreemarkerUtil=statics['com.hlgf.sms.utils.FreemarkerUtil']>
<#assign ctx=request.getContextPath()>
<!doctype html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8"/>
    <title>后台管理</title>
    <link rel="stylesheet" type="text/css" href="${ctx }/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx }/css/main.css"/>
    <script type="text/javascript" src="${ctx }/js/jquery.min.1.91.js"></script>
</head>
<body>
<div class="container clearfix">

    <div class="main-wrap">

        <div class="result-wrap">
            <div class="result-content">
                <table class="result-tab" align="center">
                    <caption style="font-size:36px">企业通知设置</caption>
                    <tr>
                        <td rowspan="2" style="text-align: center;font-weight: bold;width: 190px">组织ID</td>
                        <td rowspan="2" style="text-align: center;font-weight: bold;width: 350px">组织名称</td>
                        <td colspan="5" style="text-align: center;font-weight: bold">提醒设置</td>
                        <td rowspan="2" style="text-align: center;font-weight: bold;width: 60px">操作</td>
                    </tr>
                    <tr>
                        <td style="text-align: center;font-weight: bold;width: 90px">待办通知</td>
                        <td style="text-align: center;font-weight: bold;width: 90px">预警通知</td>
                        <td style="text-align: center;font-weight: bold;width: 90px">到款通知</td>
                        <td style="text-align: center;font-weight: bold;width: 140px">销售订单通知</td>
                        <td style="text-align: center;font-weight: bold;width: 135px">余额提醒(季度)</td>
                    </tr>
                        <#list all as item>
                        <tr>
                            <td>${item.pkGroup}</td>
                            <td>${item.groupName}</td>
                            <td style="text-align: center"><input type="checkbox" id="work_${item.pkGroup}"
                                                                  <#if (FreemarkerUtil.bitAnd(item.state,1) != 0)>checked</#if>>待办通知
                            </td>
                            <td style="text-align: center"><input type="checkbox" id="alert_${item.pkGroup}"
                                                                  <#if (FreemarkerUtil.bitAnd(item.state,2) != 0)>checked</#if>>预警通知
                            </td>
                            <td style="text-align: center"><input type="checkbox" id="found_${item.pkGroup}"
                                                                  <#if (FreemarkerUtil.bitAnd(item.state,4) != 0)>checked</#if>>到款通知
                            </td>
                            <td style="text-align: center"><input type="checkbox" id="sale_${item.pkGroup}"
                                                                  <#if (FreemarkerUtil.bitAnd(item.state,8) != 0)>checked</#if>>销售订单通知
                            </td>
                            <td style="text-align: center"><input type="checkbox" id="balance_${item.pkGroup}"
                                                                  <#if (FreemarkerUtil.bitAnd(item.state,16) != 0)>checked</#if>>余额提醒(季度)
                            </td>
                            <td style="text-align: center"><a class="link-update"
                                                              href="javascript:updateSetting('${item.pkGroup}');">更新</a>
                            <input type="hidden" id="oldstate_${item.pkGroup}" value="${item.state}"/>
                            </td>
                        </tr>
                        </#list>
                </table>
            </div>
        </div>
    </div
</div>
</body>
<script type="text/javascript">
    function updateSetting(pk) {
        var work = $('#' + "work_" + pk).prop('checked');
        var prealert = $('#' + "alert_" + pk).prop('checked');
        var sale = $('#' + "sale_" + pk).prop('checked');
        var found = $('#' + "found_" + pk).prop('checked');
        var balance = $('#' + "balance_" + pk).prop('checked');
        var oldstate = $('#' + "oldstate_" + pk).val();
        var state = 0;
        if (work) state += 1;
        if (prealert) state += 2;
        if (found) state += 4;
        if (sale) state += 8;
        if (balance) state += 16;

        if(oldstate == state){
            alert("没有任何改动");
            return false;
        }

        var targetUrl = "${ctx }/updateSetting";
        var data = {"pkGroup": pk, "state": state};
        $.ajax({
            type: 'post',
            url: targetUrl,
            cache: false,
            data: data,
            dataType: 'json',
            success: function (data) {
                if (data.res == true)
                    alert("更新成功");
                else if (data == false)
                    alert('更新失败：' + data.msg);
            },
            error: function () {
                alert("请求失败")
            }
        })
    }
</script>
</html>