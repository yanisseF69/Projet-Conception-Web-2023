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
<jsp:useBean id="user" scope="request" type="fr.univlyon1.m1if.m1if03.classes.User"/>
<jsp:useBean id="userLogin" scope="request" type="java.lang.String"/>
<jsp:useBean id="userName" scope="request" type="java.lang.String"/>
<jsp:useBean id="loginSession" scope="request" type="java.lang.String"/>
<h2>Utilisateur ${userLogin}</h2>
<div>
    Login : ${userLogin}<br>
    <jsp:useBean id="login" beanName="login" scope="session" type="java.lang.String"/>
    <c:if test="${loginSession.equals(userLogin)}">
        <form method="post" action="user" target="_parent">
            <label for="name">Prénom : <input type="text" name="name" id="name" value="${userName}"></label>&nbsp;
            <input type="submit" value="Modification">
            <input type="hidden" name="operation" value="modif">
        </form>
    </c:if>
    <br>
    Todos:
    <ul>
        <jsp:useBean id="userTodos" scope="request" type="java.util.Collection<fr.univlyon1.m1if.m1if03.classes.Todo>"/>
        <c:if test="${userTodos != null}">
        <c:forEach items="${userTodos}" var="todo">
                <li><a href="todolist#${todo.hashCode()}">${todo.getTitle()}</a></li>
        </c:forEach>
        </c:if>
    </ul>
</div>
</body>
</html>