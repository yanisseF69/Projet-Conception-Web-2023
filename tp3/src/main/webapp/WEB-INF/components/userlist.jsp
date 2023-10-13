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
<jsp:useBean id="users" scope="application" type="fr.univlyon1.m1if.m1if03.daos.UserDao"/>
<h2>Liste des utilisateurs connectés</h2>
<p>Il y a actuellement ${users.findAll().size()} utilisateur(s) connect&eacute;(s) :</p>
<ul>
    <c:forEach items="${users.findAll()}" var="u">
        <li>${u.login} : <strong><a href="userDetails?user=${u.login}">${u.name}</a></strong></li>
    </c:forEach>
</ul>
</body>
</html>
