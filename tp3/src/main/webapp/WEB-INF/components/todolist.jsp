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
<h2>Liste des TODOs</h2>
<table>
    <tr>
        <th></th>
        <th>Titre</th>
        <th>Utilisateur assigné</th>
    </tr>
    <jsp:useBean id="users" scope="request" type="java.util.Collection<fr.univlyon1.m1if.m1if03.classes.User>"/>
    <jsp:useBean id="todos" scope="request" type="java.util.Collection<fr.univlyon1.m1if.m1if03.classes.Todo>"/>
    <c:forEach var="todo" items="${todos}" varStatus="loop">
            <form method="POST" action="todolist">
                <tr id="${todo.hashCode()}">
                    <td>${todo.completed ? "&#x2611;" : "&#x2610;"}</td>
                    <td><em>${todo.title}</em></td>
                    <td>
                        <c:if test="${todo.assignee != null}">

                            <a href="user?user=${todo.assignee}">${todo.assignee}</a>
                        </c:if>
                        <jsp:useBean id="user" scope="session" type="fr.univlyon1.m1if.m1if03.classes.User"/>
                        <c:if test="${!todo.completed && todo.assignee != user.login}">
                            <input type='submit' name='assign' value='Choisir cette tâche'>&nbsp;
                        </c:if>
                    </td>
                    <td>
                        <input type='submit' name='toggle' value='${todo.completed ? "Not done!" : "Done!"}'>
                    </td>
                </tr>
                <input type='hidden' name='operation' value='update'>
                <input type='hidden' name='index' value='${loop.index}'>
            </form>
        </c:forEach>
</table>
<script>
    if(location.hash) {
        document.getElementById(location.hash.substring(1)).style = "color: red";
    }
</script>
</body>
</html>