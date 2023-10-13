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
<jsp:useBean id="users" scope="application" type="fr.univlyon1.m1if.m1if03.daos.UserDao"/>
<c:set var="user" value="${users.findOne(param.user)}" scope="request"/>
<h2>Utilisateur ${user.getLogin()}</h2>
<div>
    Login : ${user.getLogin()}<br>
    <jsp:useBean id="login" beanName="login" scope="session" type="java.lang.String"/>
    <c:if test="${login.equals(user.getLogin())}">
        <form method="post" action="user" target="_parent">
            <label for="name">Prénom : <input type="text" name="name" id="name" value="${user.name}"></label>&nbsp;
            <input type="submit" value="Modification">
            <input type="hidden" name="operation" value="modif">
        </form>
    </c:if>
    <br>
    Todos:
    <ul>
        <jsp:useBean id="todos" scope="application" type="fr.univlyon1.m1if.m1if03.daos.TodoDao"/>
        <c:forEach items="${todos.findAll()}" var="todo">
            <c:if test="${todo.getAssignee() != null && todo.getAssignee().equals(user.getLogin())}">
                <li><a href="todolist#${todo.hashCode()}">${todo.getTitle()}</a></li>
            </c:if>
        </c:forEach>
    </ul>
</div>
</body>
</html>