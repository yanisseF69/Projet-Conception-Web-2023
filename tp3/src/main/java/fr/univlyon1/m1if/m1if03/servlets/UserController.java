package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.daos.UserDao;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import java.io.IOException;

/**
 * Classe qui nous servira de controlleur de l'entité User.
 */
@WebServlet(name = "UserController", value = {"/user", "/connect", "/userDetails", "/userProfile"})
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
                users.deleteById(login);
            } catch (NameNotFoundException e) {
                throw new RuntimeException(e);
            }
            request.getRequestDispatcher("/index.html").forward(request, response);
        } else if("details".equals(operation)) {
            request.getRequestDispatcher("/userlist.jsp").forward(request, response);
        } else if(request.getParameter("user") != null) {
            request.getRequestDispatcher("/user.jsp").forward(request, response);
            // On redirige la totalité de l'interface pour afficher le nouveau nom dans l'interface
        }



        request.getRequestDispatcher("/user.jsp").forward(request, response);
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
        } else if ("login".equals(operation)) {
            User user = new User(request.getParameter("login"), request.getParameter("name"));
            try {
                users.add(user);
                session = request.getSession(true);
                session.setAttribute("user", user);
            } catch (NameAlreadyBoundException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Un utilisateur avec le login " + user.getLogin() + " existe déjà.");
                return;
            }
            request.getRequestDispatcher("/interface.jsp").forward(request, response);
            return;
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Operation inconnue ou incorrecte.");
        }

        request.getRequestDispatcher("/interface.jsp").forward(request, response);
    }
}
