package fr.univlyon1.m1if.m1if03.daos;

import fr.univlyon1.m1if.m1if03.classes.Todo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe TodoDao.
 */
public class TodoDao extends AbstractListDao<Todo> {
    @Override
    protected Serializable getKeyForElement(Todo element) {
        return element.hashCode();
    }

    public List<Todo> findByAssignee(String name) {
        List<Todo> res = new ArrayList<>();
        for(Todo todo: this.findAll()) {
            if(todo.getAssignee().equals(name)) {
                res.add(todo);
            }
        }
        return res;
    }


}
