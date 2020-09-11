<%--
  Created by IntelliJ IDEA.
  User: kisska
  Date: 22.07.2020
  Time: 14:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<%@page session="false" %>
<html>
<head>
    <title>color</title>
    <link href="/css/color.css" rel="stylesheet" type="text/css">
</head>
<body>
<span style="color: ${color}">COLOR</span>
<form method="post" action="/user">
    <p><strong>^ Choose Color ^</strong></p>
    <select class="button-6" name="color" id="select" >
        <option value="red" >Red</option>
        <option value="black">Black</option>
        <option value="white">White</option>
        <option value="blue">Blue</option>
        <option value="green">Green</option>
        <option value="purple">Purple</option>
    </select>
    </label>
    <button class="button-6"><span>Click</span></button>
</form>
<script>
    var name = "<%= request.getAttribute("color") %>";
    var errorId = "<%= request.getAttribute("errorId") %>";
    var idUser = "<%= request.getAttribute("idUser") %>";
    var select = document.getElementById('select');
    var input = document.getElementById('inputId');

    select.value = name;

    if(new Boolean(errorId)){
        console.log(!!errorId, 'errorId');
        input.style.borderColor = 'red';
    }else{
        console.log(!!errorId, 'errorId else');
    }
</script>
<div class>
    <div>
        <h2> All users</h2>
    </div>
    <table>
        <tr>
            <th>ID</th>
            <th>Login</th>
            <th>Password</th>
            <th>E-mail</th>
        </tr>
        <c:forEach items="${UsersFromServer}" var="user">
            <tr>
                <td>${user.id}</td>
                <td>${user.login}</td>
                <td>${user.password}</td>
                <td>${user.email}</td>
            </tr>
        </c:forEach>
    </table>
    <form method="post" action="/userId">
        <p><b>ID user</b><br>
            <input type="number" size="10"name="id" id="inputId" minlength="1">
        </p>
        <button class="button-6" id="button"><span>Delete</span></button>
    </form>
</div>
</body>
</html>