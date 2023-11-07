package fr.univlyon1.m1if.m1if03.controllers;

import fr.univlyon1.m1if.m1if03.dao.TodoDao;
import fr.univlyon1.m1if.m1if03.dto.todo.TodoDtoMapper;
import fr.univlyon1.m1if.m1if03.dto.todo.TodoResponseDto;
import fr.univlyon1.m1if.m1if03.exceptions.ForbiddenLoginException;
import fr.univlyon1.m1if.m1if03.model.Todo;
import fr.univlyon1.m1if.m1if03.utils.UrlUtils;
import jakarta.servlet.ServletConfig;
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
    private TodoDao todoDao;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        todoDao = (TodoDao) config.getServletContext().getAttribute("todoDao");
        todoMapper = new TodoDtoMapper(config.getServletContext());
        todoRessource = new TodoRessource(todoDao);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] url = UrlUtils.getUrlParts(request);

        if (url.length == 1) { // renvoie la liste de tout les todos (je crois...)
            request.setAttribute("todos", todoRessource.readAll());
            request.getRequestDispatcher("/WEB-INF/components/todos.jsp").include(request, response);
            return;
        }
        try {
            Todo todo = todoRessource.readOne(url[1]);
            TodoResponseDto todoDto = todoMapper.toDto(todo);
            switch (url.length) {
                case 2: // renvoie un DTO de Todo (avec toutes les infos le concernant pour pouvoir le templater dans la vue)
                        request.setAttribute("todoDto", todoDto);
                        request.getRequestDispatcher("/WEB-INF/components/todo.jsp").include(request, response);
                    break;
                case 3: // renvoie une propriété d'un todo
                    switch (url[2]) {
                        case "title":
                            request.setAttribute("todoDto", new TodoResponseDto(todoDto.getTitle(), todoDto.getHash(), null, null));
                            request.getRequestDispatcher("/WEB-INF/components/todo.jsp").include(request, response);
                            break;
                        case "hash":
                            request.setAttribute("todoDto", new TodoResponseDto(null, todoDto.hashCode(), null, null));
                            request.getRequestDispatcher("/WEB-INF/components/todo.jsp").include(request, response);
                            break;
                        case "assignee":
                            request.setAttribute("todoDto", new TodoResponseDto(null, todoDto.getHash(), todoDto.getAssignee(), null));
                            request.getRequestDispatcher("/WEB-INF/components/todo.jsp").include(request, response);
                            break;
                        case "status":
                            request.setAttribute("todoDto", new TodoResponseDto(null, todoDto.getHash(), null, todoDto.getCompleted()));
                            request.getRequestDispatcher("/WEB-INF/components/todo.jsp").include(request, response);
                            break;
                        default:
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST);

                    }
                    break;
                default: // redirige vers l'URL qui devrait correspondre à la sous-priorité demandée (qu'elle existe ou pas ne concerne pas ce contrôleur)
                    // Construction de la fin de l'URL vers laquelle rediriger
                    if (url[2].equals("assignee")) {
                        // Construction de la fin de l'URL vers laquelle rediriger
                        String urlEnd = UrlUtils.getUrlEnd(request, 3);
                        response.sendRedirect(request.getContextPath() + "/users" + urlEnd);
                    } else {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Trop de paramètres dans l'URI.");
                    }

            }
        } catch (IllegalArgumentException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        } catch (InvalidNameException | NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] url = UrlUtils.getUrlParts(request);

        switch (url.length) {
            case 1:
                String creator = request.getParameter("creator");
                String title = request.getParameter("title");
                try {
                    int id = todoRessource.create(title, creator);
                    response.setHeader("Location", "todos/" + id);
                    response.setStatus(HttpServletResponse.SC_CREATED);
                } catch (NameAlreadyBoundException | ForbiddenLoginException e) {
                    throw new RuntimeException(e);
                }

                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] url = UrlUtils.getUrlParts(request);
        String todoId = url[1];

        String title = request.getParameter("title");
        String assignee = request.getParameter("login");

        if (url.length == 2) {
            try {
                todoRessource.update(todoId, title, assignee);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (IllegalArgumentException | NameNotFoundException | InvalidNameException ex) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] url = UrlUtils.getUrlParts(request);
        String key = url[1];
        if (url.length == 2) {
            try {
                todoRessource.delete(key);
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

        public int create(@NotNull String title, String creator)
                throws IllegalArgumentException, NameAlreadyBoundException, ForbiddenLoginException {
            if (title == null || title.isEmpty()) {
                throw new IllegalArgumentException("Le titre ne doit pas être null ou vide.");
            }
            return todoDao.add(new Todo(title, creator));
        }

        public Collection<Todo> readAll() {
            return todoDao.findAll();
        }

        public Todo readOne(@NotNull String key) throws IllegalArgumentException, NameNotFoundException, InvalidNameException {
            if (key == null || key.isEmpty()) {
                throw new IllegalArgumentException("L'id du message ne doit pas être null ou vide.");
            }
            return todoDao.findOne(Integer.parseInt(key));
        }



        public void delete(String key) throws IllegalArgumentException, NameNotFoundException, InvalidNameException {
            if (key == null || key.isEmpty()) {
                throw new IllegalArgumentException("Le login ne doit pas être null ou vide.");
            }
            todoDao.delete(todoDao.findOne(Integer.parseInt(key)));
        }

        public void update(String oldTitle, String title, String assignee) throws IllegalArgumentException, InvalidNameException, NameNotFoundException {
            Todo todo = readOne(oldTitle);
            todo.setTitle(title);
            todo.setAssignee(assignee);
        }
    }
}

