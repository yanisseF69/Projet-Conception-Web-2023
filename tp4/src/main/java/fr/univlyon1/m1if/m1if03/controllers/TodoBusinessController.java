package fr.univlyon1.m1if.m1if03.controllers;

import fr.univlyon1.m1if.m1if03.dao.TodoDao;
import fr.univlyon1.m1if.m1if03.dto.todo.TodoRequestDto;
import fr.univlyon1.m1if.m1if03.model.Todo;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;

import javax.naming.InvalidNameException;
import java.io.IOException;

/**
 * Contrôleur d'opérations métier "users".<br>
 * Concrètement : gère les opérations de login et de logout.
 *
 * @author Lionel Médini
 */
@WebServlet(name = "TodoBusinessController", urlPatterns = {"/todos/toggleStatus"})
public class TodoBusinessController extends HttpServlet {
    private TodoBusiness todoBusiness;
    private TodoDao todoDao;

    //<editor-fold desc="Méthode de gestion du cycle de vie">
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        todoDao = (TodoDao) config.getServletContext().getAttribute("todoDao");
        todoBusiness = new TodoBusiness(todoDao);
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
        if (request.getRequestURI().endsWith("toggleStatus")) {

            TodoRequestDto body = (TodoRequestDto) request.getAttribute("dto");

            String hash = request.getParameter("hash");
                try {
                        todoBusiness.changeStatus(Integer.parseInt(hash));
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    } catch (InvalidNameException e) {
                    throw new RuntimeException(e);
            }
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
    private static class TodoBusiness {
        private final TodoDao todoDao;

        /**
         * Constructeur avec une injection du DAO nécessaire aux opérations.
         * @param todoDao le DAO d'utilisateurs provenant du contexte applicatif
         */
        TodoBusiness(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        public void changeStatus(@NotNull int hash) throws InvalidNameException {
            Todo todo = todoDao.findByHash(hash);
            todo.setCompleted(!todo.isCompleted());
            todoDao.update(todoDao.getId(todo), todo);
        }
    }


}
