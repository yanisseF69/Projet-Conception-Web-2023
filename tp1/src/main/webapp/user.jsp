<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<!DOCTYPE html>

<jsp:useBean id="user" beanName="user" scope="session"
             type="fr.univlyon1.m1if.m1if03.classes.User"/>

<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>TODOs</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<h1>Profil de ${user.name}</h1>
<form method="post" action="todos">
    <p>
        Modifiez votre nom :<br>
        <label>Login : <input type="text" name="login" value="${user.login}" readonly ></label><br>
        <label>Prénom : <input type="text" name="name" value="${user.name}"></label><br>
        <input type="submit" value="Modifier">
    </p>
</form>
</body>
</html>