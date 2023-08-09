package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.Todo;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette servlet gère la liste des TODOs.
 * Elle permet actuellement d'afficher la liste et de créer de nouveaux TODOs.
 * Elle devra aussi permettre de modifier l'état d'un TODO_
 */
@WebServlet(name = "TodoList", value = "/todolist")
public class TodoList extends HttpServlet {
    private final List<Todo> todos = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Génère "à la main" la page HTML de réponse
        PrintWriter out = response.getWriter();
        out.write("<!DOCTYPE html>\n" +
                "<html lang=\"fr\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>TODOs</title>\n" +
                "    <link rel=\"stylesheet\" href=\"css/style.css\">\n" +
                "    <meta http-equiv=\"refresh\" content=\"5\">\n" +
                "</head>\n" +
                "<body>\n" +
                "    <table>");
        for(Todo t: todos) {
            out.println("        <form action='/updateTodo' method='POST'>");
            out.write("          <tr><td><em>" + (t.isCompleted()?"&#x2611;":"&#x2610;") + "</em></td>");
            out.write("<td><em>" + t.getTitle() + "</em></td>");
            if(t.isCompleted()) {
                out.write("<td><input type='submit' name='toggle' value='Not done!'></td>");
            } else {
                out.write("<td><input type='submit' name='toggle' value='Done!'></td>");
            }
            out.println("</tr>");
            out.println("          </input type='hidden' name='todo' value=" + t.hashCode() +">");
            out.println("        </form>");
        }
        out.write("    </table>\n" +
                "</body>\n" +
                "</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // crée un nouveau TODO_ et l'ajoute à la liste
        Todo t = new Todo(request.getParameter("title"), request.getParameter("login"));
        todos.add(t);
        // Reprend le comportement des requêtes GET
        doGet(request, response);
    }
}
