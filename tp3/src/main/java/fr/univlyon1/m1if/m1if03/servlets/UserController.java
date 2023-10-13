package fr.univlyon1.m1if.m1if03.servlets;

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

import javax.naming.InvalidNameException;
import javax.naming.NameNotFoundException;
import java.io.IOException;

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
        HttpSession session;

        UserDao users = (UserDao) context.getAttribute("users");
        String operation = request.getParameter("operation");

        if ("logout".equals(operation)) {
            session = request.getSession(false);
            String login = (String) session.getAttribute("login");
            session.invalidate();
            try {
                TodoDao todos = (TodoDao) context.getAttribute("todos");
                todos.unassign(login);
                users.deleteById(login);
            } catch (NameNotFoundException | InvalidNameException e) {
                throw new RuntimeException(e);
            }
            request.getRequestDispatcher("index.html").forward(request, response);
        } else if("details".equals(operation)) {
            request.setAttribute("users", users);
            request.getRequestDispatcher("/WEB-INF/components/userlist.jsp").include(request, response);
            return;
        } else if(request.getParameter("user") != null) {
            request.getRequestDispatcher("/WEB-INF/components/user.jsp").forward(request, response);
            return;
            // On redirige la totalité de l'interface pour afficher le nouveau nom dans l'interface
        }

        request.getRequestDispatcher("/WEB-INF/components/user.jsp").forward(request, response);
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
