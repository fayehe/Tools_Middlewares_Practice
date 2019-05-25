<%--
  Created by IntelliJ IDEA.
  User: xiaomi
  Date: 2018/4/22
  Time: 14:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>listCategory</title>
    <script type="text/javascript" src="js/jquery.min.js"></script>
    <script type="text/javascript">
        /*将post method 改变为delete*/
        $(function(){
            $(".delete").click(function(){
                var href=$(this).attr("href");
                $("#formdelete").attr("action",href).submit();
                return false;
            })
        })
    </script>
</head>
<body>
    <div align="center"></div>
    <div style="width:500px;margin:20px auto;text-align: center">
        <table align='center' border='1' cellspacing='0'>
            <tr>
                <td>id</td>
                <td>name</td>
                <td>编辑</td>
                <td>删除</td>
            </tr>
            <c:forEach items="${page.content}" var="c" varStatus="st">
                <tr>
                    <td>${c.id}</td>
                    <td>${c.name}</td>
                    <td><a href="category/${c.id}">编辑</a></td>
                    <td><a href="category/${c.id}" class="delete">删除</a></td>
                </tr>
            </c:forEach>
        </table>
        <br>
        <div>
            <%--前面没有斜线就是相对当前路径加上这个地址。--%>
            <a href="?start=0">[首  页]</a>
            <a href="?start=${page.number-1}">[上一页]</a>
            <a href="?start=${page.number+1}">[下一页]</a>
            <a href="?start=${page.totalPages-1}">[末  页]</a>
        </div>
        <br>
        <form action="category" method="post">
            <%--虽然这个form的method是post, 但是spingboot看到这个_method的值是put后，会把其修改为put.--%>
            <input type="hidden" name="_method" value="PUT">
            Add category name: <input name="name">
            <button type="submit">提交</button>
        </form>
        <form id="formdelete" action="" method="POST" >
            <input type="hidden" name="_method" value="DELETE">
        </form>
    </div>
</body>
</html>
