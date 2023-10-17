package fr.univlyon1.m1if.m1if03.controllers;

import fr.univlyon1.m1if.m1if03.dao.TodoDao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

/**
 * Contrôleur d'opérations métier "Todos".<br>
 * Concrètement : gère les opérations de login et de logout.
 *
 * @author Yanisse Ferhaoui
 */
@WebServlet(name = "TodoRessourceController", urlPatterns = {"/todos", "/todos/*"})
public class TodoResourceController extends HttpServlet {

    public void doGet() {

    }

    public void doPost() {

    }

    public void doPut() {

    }

    public void doDelete() {

    }

    private static class TodoRessource {
        private final TodoDao todoDao;

        TodoRessource(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        public void create() {

        }

    }
}

