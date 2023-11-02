package fr.univlyon1.m1if.m1if03.controllers;

import fr.univlyon1.m1if.m1if03.dao.TodoDao;
import fr.univlyon1.m1if.m1if03.dto.todo.TodoDtoMapper;
import fr.univlyon1.m1if.m1if03.dto.todo.TodoResponseDto;
import fr.univlyon1.m1if.m1if03.exceptions.ForbiddenLoginException;
import fr.univlyon1.m1if.m1if03.model.Todo;
import fr.univlyon1.m1if.m1if03.model.User;
import fr.univlyon1.m1if.m1if03.utils.UrlUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;

import javax.naming.InvalidNameException;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

/**
 * Contrôleur d'opérations métier "Todos".<br>
 * Concrètement : gère les opérations de login et de logout.
 *
 * @author Yanisse Ferhaoui
 */
@WebServlet(name = "TodoRessourceController", urlPatterns = {"/todos", "/todos/*"})
public class TodoResourceController extends HttpServlet {

    private TodoRessource todoRessource;
    private TodoDtoMapper todoMapper;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] url = UrlUtils.getUrlParts(request);

        if (url.length == 1) { // renvoie la liste de tout les todos (je crois...)
            request.setAttribute("todos", todoRessource.readAll());

            request.getRequestDispatcher("/WEB-INF/components/todos.jsp").include(request,response);

            return;
        }
        try {
            Todo todo = todoRessource.readOne(url[1]);
            TodoResponseDto todoDto = todoMapper.toDto(todo);
            switch (url.length) {
                case 2: // renvoie un DTO de Todo (avec toutes les infos le concernant pour pouvoir le templater dans la vue)
                        request.setAttribute("todoDto", todoDto);
                        request.getRequestDispatcher("/WEB-INF/components/user.jsp").include(request, response);
                    break;
                case 3: // renvoie une propriété d'un todo
                    switch (url[2]) {
                        case "title":
                            request.setAttribute("todoDto", new TodoResponseDto(todoDto.getTitle(), todoDto.getHash(), null));
                            request.getRequestDispatcher("/WEB-INF/components/").include(request, response);
                            break;
                        case "assignee":
                            request.setAttribute("todoDto", new TodoResponseDto(null, todoDto.getHash(), todoDto.getAssignee()));
                            request.getRequestDispatcher("/WEB-INF/components/").include(request, response);
                            break;
                        default:
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST);

                    }
                    break;
                default: // redirige vers l'URL qui devrait correspondre à la sous-priorité demandée (qu'elle existe ou pas ne concerne pas ce contrôleur)
                    // Construction de la fin de l'URL vers laquelle rediriger
                    String urlEnd = UrlUtils.getUrlEnd(request, 3);
                    switch (url[2]) {
                        case "owner":

                            break;
                        case "members":

                            break;
                        case "messages":

                            break;
                        default:
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    }

            }
        } catch (IllegalArgumentException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        } catch (InvalidNameException e) {
            throw new RuntimeException(e);
        } catch (NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] url = UrlUtils.getUrlParts(request);

        switch (url.length) {
            case 1:
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] url = UrlUtils.getUrlParts(request);
        String todo = url[1];

        String title = request.getParameter("title");
        String assignee = request.getParameter("login");

        if (url.length == 2) {
            try {
                todoRessource.update(title, assignee);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (IllegalArgumentException ex) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
            } catch (NameNotFoundException | InvalidNameException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] url = UrlUtils.getUrlParts(request);
        String todoId = url[1];
        if (url.length == 2) {
            try {
                todoRessource.delete(todoId);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);

            } catch (IllegalArgumentException | NameNotFoundException | InvalidNameException ex) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private static class TodoRessource {
        private final TodoDao todoDao;

        TodoRessource(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        public void create(@NotNull String title)
                throws IllegalArgumentException, NameAlreadyBoundException, ForbiddenLoginException {
            if (title == null || title.isEmpty()) {
                throw new IllegalArgumentException("Le titre ne doit pas être null ou vide.");
            }
            todoDao.add(new Todo(title));
        }

        public Collection<Todo> readAll() {
            return todoDao.findAll();
        }

        public Todo readOne(@NotNull String login) throws IllegalArgumentException, NameNotFoundException, InvalidNameException {
            if (login == null || login.isEmpty()) {
                throw new IllegalArgumentException("Le login ne doit pas être null ou vide.");
            }
            return todoDao.findOne(login);
        }

        public void delete(@NotNull String login) throws IllegalArgumentException, NameNotFoundException, InvalidNameException {
            if (login == null || login.isEmpty()) {
                throw new IllegalArgumentException("Le login ne doit pas être null ou vide.");
            }
            todoDao.deleteById(login);
        }

        public void update(String title, String assignee) throws IllegalArgumentException, InvalidNameException, NameNotFoundException {
            Todo todo = readOne("title");
            todo.setAssignee(assignee);
        }
    }
}

