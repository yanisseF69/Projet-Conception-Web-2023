package fr.univlyon1.m1if.m1if03.filters;

import fr.univlyon1.m1if.m1if03.dao.UserDao;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * UserCache class.
 */
@WebFilter(filterName = "UserCache", urlPatterns = {"/users"})
public class UserCache extends HttpFilter {


    private Map<String, String> userTags = new HashMap<>();
    private UserDao userDao;

    public void init(FilterConfig config) throws ServletException {
        super.init(config);
    }


    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        //requête GET
        if (request.getMethod().equals("GET")) {
            userDao = (UserDao) request.getServletContext().getAttribute("userDao");
            String login = (String) request.getAttribute("login");
            String userTag = getTag(login);
            String lastETAG = request.getHeader("If-None-Match");
            System.out.println("oueoue");
            if (lastETAG != null) {
                if (lastETAG.equals(userTags.get(login))) {
                    // Si les 2 chaînes matchent, 304 et retourner une page vide.
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    return;
                }
            }
            response.setHeader("ETag", userTag);
            userTags.put(login, userTag);
        }
        chain.doFilter(request, response);
    }



    private String getTag(String login) {
        return Integer.toString(login.hashCode());
    }



}
