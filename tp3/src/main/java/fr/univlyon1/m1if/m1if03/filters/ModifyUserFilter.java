package fr.univlyon1.m1if.m1if03.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


/**
 * Classe filtre.
 */
@WebFilter(filterName = "ModifyUserFilter", urlPatterns = {"/userlist"})
public class ModifyUserFilter extends HttpFilter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String currentUserLogin = (String) session.getAttribute("login");
            String userToModifyLogin = request.getParameter("login");

            if (currentUserLogin != null && currentUserLogin.equals(userToModifyLogin)) {
                // L'utilisateur actuel a le droit de modifier l'utilisateur en question
                chain.doFilter(request, response);
            } else {
                // L'utilisateur n'a pas le droit de modifier l'utilisateur en question, renvoyer une erreur 403 (Forbidden)
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Vous n'avez pas le droit de modifier cet utilisateur.");
            }
        } else {
            // L'utilisateur n'est pas connecté, renvoyer une erreur 403 (Forbidden)
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Vous n'êtes pas connecté.");
        }
    }

    @Override
    public void destroy() {
    }
}
