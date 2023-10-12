<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>User</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<c:set var="user" value="${applicationScope.users.findOne(param.user)}" scope="request"/>
<h2>Utilisateur ${user.getLogin()}</h2>
<div>
    Login : ${user.getLogin()}<br>
    <c:if test="${sessionScope.login.equals(user.getLogin())}">
        <form method="post" action="user" target="_parent">
            <label for="name">Prénom : <input type="text" name="name" id="name" value="${user.name}"></label>&nbsp;
            <input type="submit" value="Modification">
            <input type="hidden" name="operation" value="modif">
        </form>
    </c:if>
    <br>
    Todos:
    <ul>
        <c:forEach items="${applicationScope.todos.findAll()}" var="todo">
            <c:if test="${todo.getAssignee() != null && todo.getAssignee().equals(user.getLogin())}">
                <li><a href="todolist#${todo.hashCode()}">${todo.getTitle()}</a></li>
            </c:if>
        </c:forEach>
    </ul>
</div>
</body>
</html>