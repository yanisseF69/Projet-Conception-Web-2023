package fr.univlyon1.m1if.m1if03.filters;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe Cache.
 */
@WebFilter(filterName = "Cache", urlPatterns = {"/todolist"})
public class Cache extends HttpFilter {
    private final Map<String, Date> todoLastModifiedMap = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String todoId = request.getParameter("todoId");

        Date date = new Date(System.currentTimeMillis());

        if (request.getMethod().equals("GET")) {
            if (todoLastModifiedMap.get(todoId) != null) {
                long lastModified = request.getDateHeader("If-Modified-Since");
                Date dateLastModified = new Date(lastModified * 500);
                Date dateLastModifiedMap = todoLastModifiedMap.get(todoId);


                if (dateLastModifiedMap.before(dateLastModified)) {
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    return;
                }

                response.setDateHeader("Last-Modified", date.getTime());

            }
            chain.doFilter(request, response);
        }

        if (request.getMethod().equals("POST")) {
            todoLastModifiedMap.put(todoId, date);
            chain.doFilter(request, response);
        }

    }

}
