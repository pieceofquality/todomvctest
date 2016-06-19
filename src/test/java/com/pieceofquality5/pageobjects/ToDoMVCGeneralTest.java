package com.pieceofquality5.pageobjects;

import com.pieceofquality5.pageobjects.pages.ToDoMVCPage;
import org.junit.Test;

import static com.pieceofquality5.pageobjects.pages.ToDoMVCPage.TaskType.ACTIVE;
import static com.pieceofquality5.pageobjects.pages.ToDoMVCPage.TaskType.COMPLETED;

public class ToDoMVCGeneralTest extends BaseTest{

    ToDoMVCPage page = new ToDoMVCPage();

    @Test

    public void testTasksFlow() {

        page.givenAtAll(ACTIVE, "1");

        page.toggle("1");
        page.assertTasks("1");

        page.filterActive();
        page.assertNoVisibleTasks();

        page.filterCompleted();
        page.assertVisibleTasks("1");

        //reopen
        page.toggle("1");
        page.assertNoVisibleTasks();
        page.assertItemsLeft(1);

        page.filterAll();
        page.assertTasks("1");

        page.toggleAll();

        page.clearCompleted();
        page.assertNoTasks();
    }

    @Test
    public void testFilterFromActiveToAll(){
        page.givenAtActive(page.aTask("1", ACTIVE), page.aTask("2", COMPLETED), page.aTask("3", ACTIVE), page.aTask("4", ACTIVE), page.aTask("5", COMPLETED));

        page.filterAll();
        page.assertTasks("1", "2", "3", "4", "5");
        page.assertItemsLeft(3);
    }

    @Test
    public void testFilterFromCompletedToActive() {
        page.givenAtCompleted(page.aTask("1", ACTIVE), page.aTask("2", COMPLETED), page.aTask("3", ACTIVE));

        page.filterActive();
        page.assertVisibleTasks("1", "3");
        page.assertItemsLeft(2);
    }

    @Test
    public void testFilterFromAllToCompleted() {
        page.givenAtAll(page.aTask("1", ACTIVE), page.aTask("2", COMPLETED), page.aTask("3", ACTIVE));

        page.filterCompleted();
        page.assertVisibleTasks("2");
        page.assertItemsLeft(2);
    }
}
