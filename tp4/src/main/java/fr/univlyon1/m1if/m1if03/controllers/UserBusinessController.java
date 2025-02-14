package fr.univlyon1.m1if.m1if03.controllers;

import fr.univlyon1.m1if.m1if03.dao.TodoDao;
import fr.univlyon1.m1if.m1if03.dao.UserDao;
import fr.univlyon1.m1if.m1if03.dto.user.UserRequestDto;
import fr.univlyon1.m1if.m1if03.model.Todo;
import fr.univlyon1.m1if.m1if03.model.User;
import fr.univlyon1.m1if.m1if03.utils.TodosM1if03JwtHelper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;

import javax.naming.InvalidNameException;
import javax.naming.NameNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Contrôleur d'opérations métier "users".<br>
 * Concrètement : gère les opérations de login et de logout.
 *
 * @author Lionel Médini
 */
@WebServlet(name = "UserBusinessController", urlPatterns = {"/users/login", "/users/logout"})
public class UserBusinessController extends HttpServlet {
    private UserBusiness userBusiness;
    private TodoDao todoDao;

    //<editor-fold desc="Méthode de gestion du cycle de vie">
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        UserDao userDao = (UserDao) config.getServletContext().getAttribute("userDao");
        userBusiness = new UserBusiness(userDao);
        todoDao = (TodoDao) config.getServletContext().getAttribute("todoDao");
    }
    //</editor-fold>

    //<editor-fold desc="Méthode de service">
    /**
     * Réalise l'opération demandée en fonction de la fin de l'URL de la requête (<code>/users/login</code> ou <code>/users/logout</code>).
     * Renvoie un code HTTP 204 (No Content) en cas de succès.
     * Sinon, renvoie une erreur HTTP appropriée.
     *
     * @param request  Voir doc...
     * @param response Voir doc...
     * @throws IOException Voir doc...
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getRequestURI().endsWith("login")) {
            UserRequestDto body = (UserRequestDto) request.getAttribute("dto");
            String login = body.getLogin();
            String password = body.getPassword();
            //String login = request.getParameter("login");
            //String password = request.getParameter("password");
            if (login != null && !login.isEmpty()) {
                try {
                    if (userBusiness.userLogin(login, password, request)) {
                        List<Integer> assignedTo = new ArrayList<Integer>();
                        for(Todo todo: todoDao.findByAssignee(login)) {
                            assignedTo.add(todo.hashCode());
                        }
                        String authToken = TodosM1if03JwtHelper.generateToken(login, assignedTo, request);
                        response.setHeader("Authorization", "Bearer " + authToken);
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    } else {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Les login et mot de passe ne correspondent pas.");
                    }
                } catch (IllegalArgumentException ex) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                } catch (NameNotFoundException e) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "L'utilisateur " + login + " n'existe pas.");
                } catch (InvalidNameException ignored) {
                    // Ne doit pas arriver car les logins des utilisateurs sont des Strings
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else if (request.getRequestURI().endsWith("logout")) {
            String authHeader = request.getHeader("Authorization");
            String token = authHeader.substring(7);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            // Ne devrait pas arriver mais sait-on jamais...
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Nested class qui interagit avec le DAO">
    /**
     * Nested class qui réalise les opérations de login et de logout d'un utilisateur.<br>
     * Cette classe devra être modifiée pour le passage en authentification stateless.
     *
     * @author Lionel Médini
     */
    private static class UserBusiness {
        private final UserDao userDao;

        /**
         * Constructeur avec une injection du DAO nécessaire aux opérations.
         * @param userDao le DAO d'utilisateurs provenant du contexte applicatif
         */
        UserBusiness(UserDao userDao) {
            this.userDao = userDao;
        }

        /**
         * Réalise l'opération de login d'un utilisateur.
         * &Agrave; condition :
         * <ul>
         *     <li>qu'un login soit présent dans la requête</li>
         *     <li>que ce login corresponde à un utilisateur déjà créé par <code>UserResourceController</code></li>
         *     <li>que le password corresponde à celui de l'utilisateur</li>
         * </ul>
         *
         * @param login    le paramètre "login" de la requête
         * @param password le paramètre "password" de la requête
         * @param request  la requête car il faudra y créer une session uniquement à certaines conditions
         * @return <code>true</code> si les login et password correspondent, <code>false</code> sinon
         * @throws IllegalArgumentException Si le login est null ou vide ou si le password est null
         * @throws InvalidNameException Ne doit pas arriver car les clés du DAO user sont des strings
         * @throws NameNotFoundException Si le login ne correspond pas à un utilisateur existant
         */
        public boolean userLogin(@NotNull String login, String password, HttpServletRequest request)
                throws IllegalArgumentException, InvalidNameException, NameNotFoundException {
            if (login == null || login.isEmpty()) {
                throw new IllegalArgumentException("Le login ne doit pas être null ou vide.");
            }
            User user = userDao.findOne(login);
            return user.verifyPassword(password);

        }

        //</editor-fold>
    }
}
