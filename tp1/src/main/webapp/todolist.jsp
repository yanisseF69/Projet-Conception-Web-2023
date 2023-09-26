<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="fr.univlyon1.m1if.m1if03.classes.Todo" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collection" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>TODOs</title>
    <link rel="stylesheet" href="css/style.css">
    <meta http-equiv="refresh" content="5">
</head>
<body>
<table>
    <%
        List<Todo> todos = ((ArrayList<Todo>) request.getAttribute("todos"));
        if(todos != null)
        for (Todo t : todos) {
    %>
    <form method="post" action="todolist">
        <tr>
            <td><%= (t.isCompleted() ? "&#x2611;" : "&#x2610;") %></td>
            <td><em><%= t.getTitle() %></em></td>
            <td>
                <%
                    if (t.isCompleted()) {
                %>
                <input type='submit' name='toggle' value='Not done!'>
                <%
                } else {
                %>
                <input type='submit' name='toggle' value='Done!'>
                <%
                    }
                %>
            </td>
        </tr>
        <input type='hidden' name='operation' value='update'>
        <input type='hidden' name='index' value='<%= todos.indexOf(t) %>'>
    </form>
    <%
        }
    %>
</table>
</body>
</html>
