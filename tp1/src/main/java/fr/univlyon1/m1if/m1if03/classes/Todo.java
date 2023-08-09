package fr.univlyon1.m1if.m1if03.classes;

/**
 * TODOs créés par les utilisateurs.
 * Un Todos comporte un titre, un statut (à faire ou fait)
 * et l'id du dernier utilisateur qui l'a créé ou modifié.
 */
public class Todo {
    private final String title;
    private String lastUser;
    private final String title;
    private boolean completed;

    public Todo(String title, String lastUser) {
        this.lastUser = lastUser;
        this.title = title;
        this.completed = false;
    }

    public String getTitle() {
        return title;
    }

    public String getLastUser() {
        return lastUser;
    }

    public void setLastUser(String lastUser) {
        this.lastUser = lastUser;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
