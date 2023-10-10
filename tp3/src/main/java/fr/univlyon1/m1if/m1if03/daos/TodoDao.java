package fr.univlyon1.m1if.m1if03.daos;

import fr.univlyon1.m1if.m1if03.classes.Todo;

import javax.naming.NameAlreadyBoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe TodoDao.
 * */
public class TodoDao extends AbstractMapDao<Todo> {

    protected Serializable getKeyForElement(Todo element) {
        return element.getTitle();
    }

    public void createTodo(String title, String creator) throws NameAlreadyBoundException {
        Todo todo = new Todo(title, creator);
        this.add(todo);
    }

    public List<String> getIdTodosByOwner(String login) {
        List<Todo> todos = new ArrayList<Todo>(this.findAll());
        List<String> idTodos = new ArrayList<String>();
        todos.forEach(todo -> {
            if(todo.getAssignee().getLogin() == login) {
                idTodos.add(todo.getAssignee().getLogin());
            }
        });
        return idTodos;
    }

    public List<Todo> getTodosByOwner(String login) {
        List<Todo> todos = new ArrayList<Todo>(this.findAll());
        List<Todo> todosOwner = new ArrayList<Todo>();
        todos.forEach(todo -> {
            if(todo.getAssignee().getLogin() == login) {
                todosOwner.add(todo);
            }
        });
        return todosOwner;
    }

    public Todo getByTitle(String title) {
        List<Todo> todos = new ArrayList<>(this.findAll());
        for(int i = 0; i < todos.size(); i++) {
            if(todos.get(i).getTitle().equals(title)) {

            }
        }
    }




}
