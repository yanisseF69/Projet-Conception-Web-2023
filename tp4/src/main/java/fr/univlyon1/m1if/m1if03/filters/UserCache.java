package fr.univlyon1.m1if.m1if03.filters;

import fr.univlyon1.m1if.m1if03.dao.UserDao;
<<<<<<< HEAD
=======
import fr.univlyon1.m1if.m1if03.dto.user.UserDtoMapper;
import fr.univlyon1.m1if.m1if03.model.User;
>>>>>>> d7cafd40cac1100990fe0a17e92ba1638e61d513
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;


import java.io.IOException;
<<<<<<< HEAD
=======
import java.util.Collection;
>>>>>>> d7cafd40cac1100990fe0a17e92ba1638e61d513
import java.util.HashMap;
import java.util.Map;

/**
 * UserCache class.
 */
@WebFilter(filterName = "UserCache", urlPatterns = {"/users"})
public class UserCache extends HttpFilter {

<<<<<<< HEAD

    private Map<String, String> userTags = new HashMap<>();
    private UserDao userDao;
=======
    private Map<String, String> userETAGs = new HashMap<>();
    private UserDao userDao;
    private UserDtoMapper userMapper;
>>>>>>> d7cafd40cac1100990fe0a17e92ba1638e61d513

    public void init(FilterConfig config) throws ServletException {
        super.init(config);
    }

<<<<<<< HEAD

=======
>>>>>>> d7cafd40cac1100990fe0a17e92ba1638e61d513
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

<<<<<<< HEAD
        //requête GET
        if (request.getMethod().equals("GET")) {
            userDao = (UserDao) request.getServletContext().getAttribute("userDao");
            String login = (String) request.getAttribute("login");
            String userTag = getTag(login);
            String lastETAG = request.getHeader("If-None-Match");
            System.out.println("oueoue");
            if (lastETAG != null) {
                if (lastETAG.equals(userTags.get(login))) {
=======
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
>>>>>>> d7cafd40cac1100990fe0a17e92ba1638e61d513
                    // Si les 2 chaînes matchent, 304 et retourner une page vide.
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    return;
                }
            }
<<<<<<< HEAD
            response.setHeader("ETag", userTag);
            userTags.put(login, userTag);
        }
        chain.doFilter(request, response);
    }



    private String getTag(String login) {
        return Integer.toString(login.hashCode());
=======
            //response.setHeader("ETAG", currentETAG);
        }
        chain.doFilter(request, response);
    }
     */



    private String getTag(Collection<User> users) {
        return String.valueOf(users.stream().map(User::getLogin).toList().toString().hashCode());
>>>>>>> d7cafd40cac1100990fe0a17e92ba1638e61d513
    }



}
