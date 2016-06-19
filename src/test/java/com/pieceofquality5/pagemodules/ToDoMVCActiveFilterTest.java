package com.pieceofquality5.pagemodules;

import org.junit.Test;

import static com.pieceofquality5.pagemodules.pages.ToDoMVCPage.TaskType.ACTIVE;
import static com.pieceofquality5.pagemodules.pages.ToDoMVCPage.*;

/**
 * Created by piece on 19.06.2016.
 */
public class ToDoMVCActiveFilterTest extends BaseTest{

    @Test
    public void testAddAtActive(){
        givenAtActive(ACTIVE, "1");

        add("2");
        assertVisibleTasks("1", "2");
        assertItemsLeft(2);
    }
    @Test
    public void testDeleteAtActive(){
        givenAtActive(ACTIVE, "1");

        delete("1");
        assertNoVisibleTasks();
    }

    @Test
    public void testEditAtActive() {
        givenAtActive(ACTIVE, "1");

        startEdit("1", "1 edited").pressEnter();
        assertVisibleTasks("1 edited");
        assertItemsLeft(1);
    }

    @Test
    public void testCompleteAtActive(){
        givenAtActive(aTask("1", ACTIVE), aTask("2", ACTIVE));

        toggle("1");
        assertVisibleTasks("2");
        assertItemsLeft(1);
    }

    @Test
    public void testEditByPressTabAtActive() {
        givenAtActive(ACTIVE, "1");

        startEdit("1", "1 edited").pressTab();
        assertTasks("1 edited");
        assertItemsLeft(1);
    }
}
