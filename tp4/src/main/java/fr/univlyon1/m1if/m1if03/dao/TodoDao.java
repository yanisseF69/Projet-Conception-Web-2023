package fr.univlyon1.m1if.m1if03.dao;

import fr.univlyon1.m1if.m1if03.model.Todo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implémentation de l'interface DAO pour la classe <code>Todo</code>.
 *
 * @author Lionel Médini
 */
public class TodoDao extends AbstractListDao<Todo> {
    /**
     * Constructeur <em>A SUPPRIMER</em> : ajoute quelques todos au démarrage pour pouvoir tester avec uniquement la partie users.
     */
    //TODO Supprimer ce constructeur une fois que la partie todos a été réalisée...
    public TodoDao() {
        super();
        //TODO Virer ça...
        this.add(new Todo("TODO1 : finir le TP3", "toto"));
        this.add(new Todo("TODO2 : faire le TP4", "toto"));
        this.add(new Todo("TODO 3 : dormir", "toto"));
    }

    /**
     * Permet de retrouver un Todo_ à partir de son hash (son "vrai" ID), puisque dans le DAO, ils sont classés par ordre.
     * @param hash Le hash du todo_ à rechercher
     * @return Un todo_ correspondant au hash, ou une <code>NoSuchElementException</code>
     */
    public Todo findByHash(int hash) {
        Optional<Todo> opt = collection.stream()
                .filter(todo -> todo.hashCode() == hash)
                .findFirst();
        return opt.orElseThrow();
    }

    /**
     * Renvoie la liste des todos auxquels un utilisateur est assigné.
     * @param assignee Le login de l'utilisateur
     * @return Une liste (potentiellement vide) de todos
     */
    public List<Todo> findByAssignee(String assignee) {
        return collection.stream()
                .filter(todo -> todo.getAssignee() != null && todo.getAssignee().equals(assignee))
                .collect(Collectors.toList());
    }
}
