<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>title</title>
</head>
<body>

    $(".time").datetimepicker({
        minView: "month",
        language:  'zh-CN',
        format: 'yyyy-mm-dd',
        autoclose: true,
        todayBtn: true,
        pickerPosition: "bottom-left"
    });

    $.ajax({

        url : "",
        data : {

        },
        type : "",
        dataType : "json",
        success : function(data){



        }

    })

    String createTime = DateTimeUtil.getSysTime();
    String createBy = ((User)request.getSession().getAttribute("user")).getName();

    String editTime = DateTimeUtil.getSysTime();
    String editBy = ((User)request.getSession().getAttribute("user")).getName();

</body>
</html>
