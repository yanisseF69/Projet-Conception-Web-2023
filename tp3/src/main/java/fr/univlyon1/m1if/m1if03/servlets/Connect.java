package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.User;

import fr.univlyon1.m1if.m1if03.daos.TodoDao;
import fr.univlyon1.m1if.m1if03.daos.UserDao;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.naming.InvalidNameException;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import java.io.IOException;



/*
@WebServlet(name = "Connect", urlPatterns = {"/connect"})
public class Connect extends HttpServlet {
    // Elles seront stockées dans le contexte applicatif pour pouvoir être accédées par tous les objets de l'application :

    // DAO d'objets User
    private final Dao<User> users = new UserDao();

    @Override
    public void init(ServletConfig config) throws ServletException {
        // Cette instruction doit toujours être au début de la méthode init() pour pouvoir accéder à l'objet config.
        super.init(config);
        //Récupère le contexte applicatif et y place les variables globales
        ServletContext context = config.getServletContext();

        // Variables communes pour toute l'application (remplacent la BD).
        // Elles seront stockées dans le contexte applicatif pour pouvoir être accédées par tous les objets de l'application :
        context.setAttribute("users", users);
        // A modifier (DAO)
        context.setAttribute("todos", new ArrayList<Todo>());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User(request.getParameter("login"), request.getParameter("name"));
        try {
            users.add(user);
        } catch (NameAlreadyBoundException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Un utilisateur avec le login " + user.getLogin() + " existe déjà.");
            return;
        }
        // Ceci est une redirection HTTP : le client est informé de cette redirection.
        response.sendRedirect("interface.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Utilise un RequestDispatcher pour "transférer" la requête à un autre objet, en interne du serveur.
        // Ceci n'est pas une redirection HTTP ; le client n'est pas informé de cette redirection.
        // Note :
        //     il existe deux méthodes pour transférer une requête (et une réponse) à l'aide d'un RequestDispatcher : include et forward
        //     voir les différences ici : https://docs.oracle.com/javaee/6/tutorial/doc/bnagi.html
        request.getRequestDispatcher("interface.jsp").forward(request, response);
    }
}
*/

/**
 * Cette servlet initialise les objets communs à toute l'application,
 * récupère les infos de l'utilisateur pour les placer dans sa session
 * et affiche l'interface du chat.
 *
 * @author Lionel Médini
 */
@WebServlet(name = "Connect", urlPatterns = {"/todos"})
public class Connect extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupère le DAO d'utilisateurs à partir du contexte
        ServletContext context = getServletContext();
        UserDao users = (UserDao) context.getAttribute("users");

        String operation = request.getParameter("operation");

        if ("login".equals(operation)) {
            User user = new User(request.getParameter("login"), request.getParameter("name"));
            try {
                users.add(user);

                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("login", user.getLogin());
                request.setAttribute("user", user);
                request.setAttribute("userName", user.getName());
                request.setAttribute("login", user.getLogin());

            } catch (NameAlreadyBoundException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Un utilisateur avec le login " + user.getLogin() + " existe déjà.");
                return;
            }
            request.getRequestDispatcher("/WEB-INF/components/interface.jsp").include(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Operation inconnue ou incorrecte.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String operation = request.getParameter("operation");

        if ("logout".equals(operation)) {
            HttpSession session = request.getSession(false);

            String login = (String) session.getAttribute("login");
            session.invalidate();
            try {
                ((TodoDao) this.getServletContext().getAttribute("todos")).unassign(login);
                ((UserDao) this.getServletContext().getAttribute("users")).deleteById(login);
            } catch (NameNotFoundException | InvalidNameException e) {
                throw new RuntimeException(e);
            }
            response.sendRedirect("index.html");

        } else {
            request.getRequestDispatcher("/WEB-INF/components/interface.jsp").forward(request, response);
        }
    }
}

