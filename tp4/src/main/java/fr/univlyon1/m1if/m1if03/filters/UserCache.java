package fr.univlyon1.m1if.m1if03.filters;

import fr.univlyon1.m1if.m1if03.dao.UserDao;
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

/**
 * UserCache class.
 */
@WebFilter(filterName = "UserCache", urlPatterns = {"/users"})
public class UserCache extends HttpFilter {

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



    private String getTag(Collection<User> users) {
        return String.valueOf(users.stream().map(User::getLogin).toList().toString().hashCode());
    }



}
