package fr.univlyon1.m1if.m1if03.filters;
import fr.univlyon1.m1if.m1if03.dao.TodoDao;
import fr.univlyon1.m1if.m1if03.model.Todo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe Cache.
 */
@WebFilter(filterName = "TodoCache", urlPatterns = {"/todos"})
public class TodoCache extends HttpFilter {
    private final Map<Collection<Todo>, Date> todoLastModifiedMap = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        TodoDao todoDao = (TodoDao) request.getServletContext().getAttribute("todoDao");
        Collection<Todo> todos = todoDao.findAll();
        Date date = new Date(System.currentTimeMillis());

        if (request.getMethod().equals("GET")) {
            if (todoLastModifiedMap.get(todos) != null) {
                long lastModified = request.getDateHeader("If-Modified-Since");
                Date dateLastModified = new Date(lastModified * 500);
                Date dateLastModifiedMap = todoLastModifiedMap.get(todos);


                if (dateLastModifiedMap.before(dateLastModified)) {
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    return;
                }

                response.setDateHeader("Last-Modified", date.getTime());

            }
            chain.doFilter(request, response);
        }

        if (request.getMethod().equals("POST")) {
            todoLastModifiedMap.put(todos, date);
            chain.doFilter(request, response);
        }

    }

}
