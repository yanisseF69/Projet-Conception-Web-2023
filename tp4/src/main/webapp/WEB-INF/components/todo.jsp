<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Message</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<h1>Message</h1>

<c:if test="${requestScope.todoDto.title != null}">
    <h2>Titre</h2>
    <p><a href="${pageContext.request.contextPath}/todos/${requestScope.todoDto.title}">${requestScope.todoDto.title}</a></p>
</c:if>

<c:if test="${requestScope.todoDto.assignee != null}">
    <h2>Assignee</h2>
    <p><a href="${pageContext.request.contextPath}/users/${requestScope.todoDto.assignee}">${requestScope.todoDto.assignee}</a></p>
</c:if>

<c:if test="${requestScope.todoDto.hash != null}">
    <h2>Texte</h2>
    <p><c:out value="${requestScope.todoDto.hash}"/></p>
</c:if>

<c:if test="${requestScope.todoDto.completed != null}">
    <h2>Texte</h2>
    <p><c:out value="${requestScope.todoDto.completed}"/></p>
</c:if>

</body>
</html>
