package fr.univlyon1.m1if.m1if03.daos;

import fr.univlyon1.m1if.m1if03.classes.Todo;

import javax.naming.InvalidNameException;
import javax.naming.NameNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe TodoDao.
 */
public class TodoDao extends AbstractListDao<Todo> {

    public List<Todo> findById(Serializable id) {
        List<Todo> res = new ArrayList<>();
        this.collection.forEach(todo -> {
            if(todo.getAssignee() == id) res.add(todo);
        });
        return res;
    }
    public void unassign(Serializable id) throws InvalidNameException, NameNotFoundException {
        findById(id).forEach(todo -> todo.setAssignee(null));
    }
}
