package com.pieceofquality.pagemodules;

import org.junit.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.pieceofquality.pagemodules.pages.ToDoMVC.TaskType.ACTIVE;
import static com.pieceofquality.pagemodules.pages.ToDoMVC.TaskType.COMPLETED;
import static com.pieceofquality.pagemodules.pages.ToDoMVC.*;

public class ToDoMVCCompletedFilterTest extends BaseTest{

    @Test
    public void testEditAtCompleted() {
        givenAtCompleted(aTask("1", ACTIVE), aTask("2", COMPLETED));

        startEdit("2", "2 edited").pressEnter();
        assertVisibleTasks("2 edited");
        assertItemsLeft(1);
    }

    @Test
    public void testClearCompletedAllAtCompleted() {
        givenAtCompleted(aTask("1", ACTIVE), aTask("2", COMPLETED));

        clearCompleted();
        assertNoVisibleTasks();
        assertItemsLeft(1);
    }

    @Test
    public void testReopenAtCompleted(){
        givenAtCompleted(COMPLETED, "1");

        toggle("1");
        assertNoVisibleTasks();
        assertItemsLeft(1);
    }

    @Test
    public void testReopenAllAtCompleted(){
        givenAtCompleted(COMPLETED, "1", "2");

        toggleAll();
        assertNoVisibleTasks();
        assertItemsLeft(2);
    }

    @Test
    public void testCancelEditAtCompleted() {
        givenAtCompleted(COMPLETED, "1");

        startEdit("1", "1 cancelled").pressEscape();
        assertVisibleTasks("1");
        assertItemsLeft(0);
    }

    @Test
    public void testEditByClickOutsideAtCompleted() {
        givenAtCompleted(COMPLETED, "1");

        startEdit("1", "1 edited");
        $("#new-todo").click();
        assertTasks("1 edited");
        assertItemsLeft(0);
    }
}
