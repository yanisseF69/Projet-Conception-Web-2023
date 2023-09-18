package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.User;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cette servlet initialise les objets communs à toute l'application,
 * récupère les infos de l'utilisateur pour les placer dans sa session
 * et affiche l'interface du chat (sans modifier l'URL).
 * &Agrave; noter le fait que l'URL à laquelle elle répond ("/todos") n'est pas le nom de la servlet.
 */
@WebServlet(name = "Deco", value = "/deco")
public class Deco extends HttpServlet {
    // Variables communes pour toute l'application (remplacent la BD).
    // Elles seront stockées dans le contexte applicatif pour pouvoir être accédées par tous les objets de l'application :

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);
        if(session != null){

            session.removeAttribute("user");

            session.invalidate();

        }
        // Ceci est une redirection HTTP ; le client est informé qu'il doit requêter une autre ressource
        response.sendRedirect("index.html");
    }
}
