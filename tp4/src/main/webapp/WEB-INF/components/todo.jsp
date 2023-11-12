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

<c:if test="${requestScope.model.title != null}">
    <h2>Titre</h2>
    <p><a href="${pageContext.request.contextPath}/todos/${requestScope.model.title}">${requestScope.model.title}</a></p>
</c:if>

<c:if test="${requestScope.model.assignee != null}">
    <h2>Assignee</h2>
    <p><a href="${pageContext.request.contextPath}/users/${requestScope.model.assignee}">${requestScope.model.assignee}</a></p>
</c:if>

<c:if test="${requestScope.model.hash != null}">
    <h2>Texte</h2>
    <p><c:out value="${requestScope.model.hash}"/></p>
</c:if>

<c:if test="${requestScope.model.completed != null && requestScope.model.completed == true}">
    <h2>Texte</h2>
    <p>&#x2610</p>
</c:if>
<c:if test="${requestScope.model.completed == null}">
    <h2>Texte</h2>
    <p>&#x2611</p>
</c:if>

</body>
</html>
