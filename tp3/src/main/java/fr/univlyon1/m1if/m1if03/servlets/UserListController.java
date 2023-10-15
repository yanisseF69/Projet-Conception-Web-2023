package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.daos.UserDao;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collection;

/**
 * Classe qui nous servira de controlleur de l'entité User.
 */
@WebServlet(name = "UserListController", urlPatterns = {"/users"})
public class UserListController extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDao users = (UserDao) getServletContext().getAttribute("users");
        Collection<User> userList = users.findAll();
        Integer userSize = userList.size();
        request.setAttribute("users", userList);
        request.setAttribute("userSize", userSize);
        request.getRequestDispatcher("/WEB-INF/components/userlist.jsp").include(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    request.getRequestDispatcher("/WEB-INF/components/interface.jsp").forward(request, response);

    }
}
