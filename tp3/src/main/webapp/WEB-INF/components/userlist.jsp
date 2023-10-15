<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>TODOs</title>
    <link rel="stylesheet" href="css/style.css">
    <meta http-equiv="refresh" content="5">
</head>
<body>
<jsp:useBean id="users" scope="request" type="java.util.Collection<fr.univlyon1.m1if.m1if03.classes.User>"/>
<jsp:useBean id="userSize" scope="request" type="java.lang.Integer"/>
<h2>Liste des utilisateurs connectés</h2>
<p>Il y a actuellement ${userSize} utilisateur(s) connect&eacute;(s) :</p>
<ul>
    <c:forEach items="${users}" var="u">
        <li>${u.login} : <strong><a href="user?user=${u.login}">${u.name}</a></strong></li>
    </c:forEach>
</ul>
</body>
</html>
