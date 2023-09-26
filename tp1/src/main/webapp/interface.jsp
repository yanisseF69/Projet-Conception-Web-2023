<%@ page import="java.util.Map" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>

<!DOCTYPE html>

<jsp:useBean id="user" beanName="user" scope="session"
             type="fr.univlyon1.m1if.m1if03.classes.User"/>

<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>TODOs</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<header>
    <h1 class="header-titre">MIF TODOs</h1>


    <p class="header-user">Bonjour <strong><a href="user.jsp">
        ${user.name}
    </a></strong>,<br>
        il y a actuellement <%=((Map<?, ?>) (application.getAttribute("users"))).size()%> utilisateur(s) connect&eacute;(s).</p>
</header>

<div class="wrapper">
    <aside class="menu">
        <h2>Menu</h2>
        <div>
            <!-- TODO -->
            <a href="deco">D&eacute;connexion</a>
        </div>
    </aside>

    <article class="contenu">
        <h2>liste des TODOs</h2>
        <iframe src="todolist" name="todolist_frame" style="border: none; width: 100%; height: 300px;"></iframe>
        <hr>
        <form method="post" action="todolist" target="todolist_frame">
            <p>
                Ajouter un TODO :
                <label>
                    <input type="text" name="title">
                </label>
                <input type="submit" value="Envoyer">
                <input type="hidden" name="operation" value="add">
                <input type="hidden" name="login" value="${sessionScope.user.login}">
            </p>
        </form>
    </article>
</div>

<footer>
    <div>Licence : <a rel="license" href="https://creativecommons.org/licenses/by-nc-sa/3.0/fr/"><img alt="Licence Creative Commons" style="border-width:0; vertical-align:middle;" src="https://i.creativecommons.org/l/by-nc-sa/3.0/fr/88x31.png" /></a></div>
</footer>
</body>
</html>