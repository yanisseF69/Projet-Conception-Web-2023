package fr.univlyon1.m1if.m1if03.classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TodoTest {
    private final Todo todo = new Todo("test TODO", "toto");

    @Test
    void getTitle() {
        assertEquals(todo.getTitle(), "test TODO");
    }

    @Test
    void getLastUser() {
        assertEquals(todo.getLastUser(), "toto");
    }

    @Test
    void setLastUser() {
        todo.setLastUser("titi");
        assertEquals(todo.getTitle(), "test message 2");
    }

    @Test
    void isCompleted() {
        assertEquals(todo.isCompleted(), false);
    }

    @Test
    void setCompleted() {
        todo.setCompleted(false);
        assertEquals(todo.isCompleted(), true);
    }
}
