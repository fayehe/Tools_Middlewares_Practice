<%--
  Created by IntelliJ IDEA.
  User: xiaomi
  Date: 2018/4/22
  Time: 19:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>edit category</title>
</head>
<body>
    <div style="margin:0px auto; width:500px">
        <form action="../category/${c.id}" method="post">
            pls edit this category name: <input name="name" value="${c.name}">
            <%--<input name="id" type="hidden" value="${c.id}">--%>
            <button type="submit">提交</button>
        </form>
    </div>
</body>
</html>
