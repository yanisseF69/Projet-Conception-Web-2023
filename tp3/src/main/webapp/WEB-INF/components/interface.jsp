<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>TODOs</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<jsp:useBean id="user" scope="session" type="fr.univlyon1.m1if.m1if03.classes.User"/>
<jsp:useBean id="users" scope="application" type="fr.univlyon1.m1if.m1if03.daos.UserDao"/>
<jsp:useBean id="login" scope="session" type="java.lang.String"/>
    <header>
        <h1 class="header-titre">MIF TODOs</h1>
        <p class="header-user">Bonjour <strong><a href="userDetails?user=${user.login}" target="list">${users.findOne(login).name}</a></strong></p>
    </header>

    <div class="wrapper">
        <aside class="menu">
            <h2>Menu</h2>
            <div>
                <a href="user?operation=details" target="list">Utilisateurs</a>
            </div>
            <div>
                <a href="todolist" target="list">Tâches</a>
            </div>
            <div>
                <a href="connect?operation=logout">D&eacute;connexion</a>
            </div>
        </aside>

        <article class="contenu">
            <iframe src="todolist" name="list" style="border: none; width: 100%; height: 300px;"></iframe>
            <hr>
            <form method="post" action="todolist" target="list">
                <p>
                    Ajouter un TODO :
                    <label>
                        <input type="text" name="title">
                    </label>
                    <input type="submit" value="Envoyer">
                    <input type="hidden" name="operation" value="add">
                    <input type="hidden" name="login" value="${user.login}">
                </p>
            </form>
        </article>
    </div>


<footer>
    <div>Licence : <a rel="license" href="https://creativecommons.org/licenses/by-nc-sa/3.0/fr/"><img alt="Licence Creative Commons" style="border-width:0; vertical-align:middle;" src="https://i.creativecommons.org/l/by-nc-sa/3.0/fr/88x31.png" /></a></div>
</footer>
</body>
</html>