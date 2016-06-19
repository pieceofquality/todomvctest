package com.pieceofquality5.pagemodules;

import org.junit.Test;

import static com.pieceofquality5.pagemodules.pages.ToDoMVC.TaskType.ACTIVE;
import static com.pieceofquality5.pagemodules.pages.ToDoMVC.TaskType.COMPLETED;
import static com.pieceofquality5.pagemodules.pages.ToDoMVC.*;

public class ToDoMVCGeneralTest extends BaseTest {

    @Test

    public void testTasksFlow() {

        givenAtAll(ACTIVE, "1");

        toggle("1");
        assertTasks("1");

        filterActive();
        assertNoVisibleTasks();

        filterCompleted();
        assertVisibleTasks("1");

        //reopen
        toggle("1");
        assertNoVisibleTasks();
        assertItemsLeft(1);

        filterAll();
        assertTasks("1");

        toggleAll();

        clearCompleted();
        assertNoTasks();
    }

    @Test
    public void testFilterFromActiveToAll(){
        givenAtActive(aTask("1", ACTIVE), aTask("2", COMPLETED), aTask("3", ACTIVE), aTask("4", ACTIVE), aTask("5", COMPLETED));

        filterAll();
        assertTasks("1", "2", "3", "4", "5");
        assertItemsLeft(3);
    }

    @Test
    public void testFilterFromCompletedToActive() {
        givenAtCompleted(aTask("1", ACTIVE), aTask("2", COMPLETED), aTask("3", ACTIVE));

        filterActive();
        assertVisibleTasks("1", "3");
        assertItemsLeft(2);
    }

    @Test
    public void testFilterFromAllToCompleted() {
        givenAtAll(aTask("1", ACTIVE), aTask("2", COMPLETED), aTask("3", ACTIVE));

        filterCompleted();
        assertVisibleTasks("2");
        assertItemsLeft(2);
    }
}
