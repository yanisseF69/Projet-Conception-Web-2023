package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.Todo;
import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.daos.TodoDao;
import fr.univlyon1.m1if.m1if03.daos.UserDao;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.naming.NameNotFoundException;
import java.io.IOException;
import java.util.Collection;

/**
 * Classe qui nous servira de controlleur de l'entité User.
 */
@WebServlet(name = "UserController", urlPatterns = {"/user"})
public class UserController extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ServletContext context = getServletContext();


        UserDao users = (UserDao) context.getAttribute("users");
        TodoDao todos = (TodoDao) context.getAttribute("todos");
        String userLogin = request.getParameter("user");
        try {
            HttpSession session = request.getSession();
            User user = users.findOne(userLogin);

            Collection<Todo> userTodos = todos.findById(user.getLogin());
            request.setAttribute("user", user);
            request.setAttribute("userLogin", user.getLogin());
            request.setAttribute("userName", user.getName());
            request.setAttribute("loginSession", ((User) session.getAttribute("user")).getLogin());
            request.setAttribute("userTodos", userTodos);
        } catch (NameNotFoundException e) {
            throw new RuntimeException(e);
        }

        request.getRequestDispatcher("/WEB-INF/components/user.jsp").include(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ServletContext context = getServletContext();
        HttpSession session;

        UserDao users = (UserDao) context.getAttribute("users");
        String operation = request.getParameter("operation");

        if("modif".equals(operation)) {
            session = request.getSession();
            User user = (User) session.getAttribute("user");
            user.setName(request.getParameter("name"));
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Operation inconnue ou incorrecte.");
        }

        request.getRequestDispatcher("/WEB-INF/components/interface.jsp").forward(request, response);
    }
}
