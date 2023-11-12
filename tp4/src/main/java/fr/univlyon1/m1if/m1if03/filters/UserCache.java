package fr.univlyon1.m1if.m1if03.filters;

import fr.univlyon1.m1if.m1if03.dao.UserDao;
import fr.univlyon1.m1if.m1if03.dto.user.UserDtoMapper;
import fr.univlyon1.m1if.m1if03.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;


import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * UserCache class.
 */
@WebFilter(filterName = "UserCache", urlPatterns = {"/users"})
public class UserCache extends HttpFilter {

    private Map<String, String> userETAGs = new HashMap<>();
    private UserDao userDao;
    private UserDtoMapper userMapper;

    public void init(FilterConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        UserDao userDao = (UserDao) request.getServletContext().getAttribute("userDao");
        String currentETAG = getTag(userDao.findAll());
        response.addHeader("ETAG", "test");
        response.setHeader("ETAG", "testt");

        //requête GET
        if (request.getMethod().equals("GET")) {
            userDao = (UserDao) request.getAttribute("users");
            userMapper = (UserDtoMapper) request.getAttribute("userMap");
            String login = (String) request.getAttribute("login");
            String userETAG = getETag(login);
            String lastETAG = request.getHeader("If-None-Match");

            if (lastETAG != null && currentETAG.equals(lastETAG)) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return;
            }
            response.setHeader("ETag", currentETAG);
            currentETAG.concat(login);
        }
        chain.doFilter(request, response);
    }

    private String getETag(String login) {
    }


    /*
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        UserDao userDao = (UserDao) request.getServletContext().getAttribute("userDao");
        String currentETAG = getTag(userDao.findAll());
        response.addHeader("ETAG", "test");
        response.setHeader("ETAG", "testt");

        //requête GET
        if (request.getMethod().equals("GET")) {

            String lastETAG = request.getHeader("If-None-Match");
            System.out.println("lastETAG : " + lastETAG);
            System.out.println("currentETAG : " + currentETAG);

            if (lastETAG != null) {
                if (currentETAG.equals(lastETAG)) {
                    // Si les 2 chaînes matchent, 304 et retourner une page vide.
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    return;
                }
            }
            //response.setHeader("ETAG", currentETAG);
        }
        chain.doFilter(request, response);
    }
     */



    private String getTag(Collection<User> users) {
        return String.valueOf(users.stream().map(User::getLogin).toList().toString().hashCode());
    }



}
