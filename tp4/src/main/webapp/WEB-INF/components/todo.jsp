<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>User</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<h1>Todo :  ${todo.title}</h1>
<ul>

    <c:if test="${requestScope.todoDto.title != null}">
        <h2>Titre</h2>
        <p><a href="${pageContext.request.contextPath}/todos/${requestScope.todoDto.title}">${requestScope.todoDto.title}</a></p>
    </c:if>

    <c:if test="${requestScope.todoDto.hash != null}">
        <h2>Numero</h2>
        <p><a href="${pageContext.request.contextPath}/todos/${requestScope.todoDto.hash}">${requestScope.todoDto.hash}</a></p>
    </c:if>

    <c:if test="${requestScope.todoDto.assignee != null}">
        <h2>Titre</h2>
        <p><a href="${pageContext.request.contextPath}/todos/${requestScope.todoDto.assignee}">${requestScope.todoDto.assignee}</a></p>
    </c:if>


</ul>
</body>
</html>
