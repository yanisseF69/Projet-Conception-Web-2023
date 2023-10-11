package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.Todo;
import fr.univlyon1.m1if.m1if03.classes.User;

import fr.univlyon1.m1if.m1if03.daos.Dao;
import fr.univlyon1.m1if.m1if03.daos.TodoDao;
import fr.univlyon1.m1if.m1if03.daos.UserDao;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

/**
 * Classe Init.
 */
@WebServlet(name = "Init", value = "/init", loadOnStartup = 1)
public class Init extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();

        Dao<User> users = new UserDao();
        Dao<Todo> todos = new TodoDao();

        context.setAttribute("users", users);
        context.setAttribute("todos", todos);
    }
}
