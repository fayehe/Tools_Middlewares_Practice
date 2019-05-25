<%--
  Created by IntelliJ IDEA.
  User: xiaomi
  Date: 2018/4/22
  Time: 20:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>upload image</title>
</head>
<body>
    <%--1. method="post" 是必须的--%>
    <%--2. enctype="multipart/form-data" 是必须的，表示提交二进制文件--%>
    <%--3. name="file" 是必须的，和后续服务端对应--%>
    <%--4. accept="image/*" 表示只选择图片--%>
    <form action="upload" method="post" enctype="multipart/form-data">
        请选择图片：<input type="file" name="file" accept="image/*">
        <input type="submit" value="上传">
    </form>
</body>
</html>
