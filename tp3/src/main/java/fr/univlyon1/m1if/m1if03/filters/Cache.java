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

@WebFilter(filterName = "Cache", urlPatterns = {"/todolist"})
public class Cache extends HttpFilter {
    Map<String, Date> todoLastModifiedMap = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String todoId = request.getParameter("todoId");

        if (todoLastModifiedMap.containsKey(todoId)) {
            Date lastModified = todoLastModifiedMap.get(todoId);
            long ifModifiedSince = request.getDateHeader("If-Modified-Since");

            if (ifModifiedSince != -1 && lastModified.getTime() <= ifModifiedSince) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return;
            }
        }

        chain.doFilter(request, response);

        if (todoId != null) {
            todoLastModifiedMap.put(todoId, new Date());
            response.setDateHeader("Last-Modified", System.currentTimeMillis());
        }
    }

}
