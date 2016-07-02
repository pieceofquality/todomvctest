package com.pieceofquality.pageobjects;

import com.pieceofquality.pageobjects.pages.ToDoMVCPage;
import org.junit.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.pieceofquality.pageobjects.pages.ToDoMVCPage.TaskType.ACTIVE;
import static com.pieceofquality.pageobjects.pages.ToDoMVCPage.TaskType.COMPLETED;
import static org.openqa.selenium.Keys.ESCAPE;

public class ToDoMVCCompletedFilterTest extends BaseTest {

    ToDoMVCPage page = new ToDoMVCPage();

    @Test
    public void testEditAtCompleted() {

        page.givenAtCompleted(page.aTask("1", ACTIVE), page.aTask("2", COMPLETED));

        page.startEdit("2", "2 edited").pressEnter();
        page.assertVisibleTasks("2 edited");
        page.assertItemsLeft(1);
    }

    @Test
    public void testClearCompletedAllAtCompleted() {
        page.givenAtCompleted(page.aTask("1", COMPLETED), page.aTask("2", COMPLETED));

        page.toggle("1");
        page.clearCompleted();
        page.assertNoVisibleTasks();
        page.assertItemsLeft(1);
    }

    @Test
    public void testReopenAtCompleted(){
        page.givenAtCompleted(COMPLETED, "1");

        page.toggle("1");
        page.assertNoVisibleTasks();
        page.assertItemsLeft(1);
    }

    @Test
    public void testReopenAllAtCompleted(){
        page.givenAtCompleted(COMPLETED, "1", "2");

        page.toggleAll();
        page.assertNoVisibleTasks();
        page.assertItemsLeft(2);
    }

    @Test
    public void testCancelEditAtCompleted() {
        page.givenAtCompleted(COMPLETED, "1");

        page.startEdit("1", "1 cancelled").sendKeys(ESCAPE);
        page.assertVisibleTasks("1");
        page.assertItemsLeft(0);
    }

    @Test
    public void testEditByClickOutsideAtCompleted() {
        page.givenAtCompleted(COMPLETED, "1");

        page.startEdit("1", "1 edited");
        $("#new-todo").click();
        page.assertTasks("1 edited");
        page.assertItemsLeft(0);
    }


}
