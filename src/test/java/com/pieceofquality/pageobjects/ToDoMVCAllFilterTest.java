package com.pieceofquality.pageobjects;

import com.pieceofquality.pageobjects.pages.ToDoMVCPage;
import org.junit.Test;

import static com.pieceofquality.pageobjects.pages.ToDoMVCPage.TaskType.ACTIVE;
import static com.pieceofquality.pageobjects.pages.ToDoMVCPage.TaskType.COMPLETED;

/**
 * Created by piece on 19.06.2016.
 */
public class ToDoMVCAllFilterTest extends BaseTest{


    ToDoMVCPage page = new ToDoMVCPage();

    @Test
    public void testAddAtAll(){
        page.given();
        page.add("1");
        page.assertTasks("1");
        page.assertItemsLeft(1);
    }

    @Test
    public void testDeleteAtAll(){
        page.givenAtAll(page.aTask("1", ACTIVE), page.aTask("2", COMPLETED));

        page.delete("1");
        page.assertTasks("2");
        page.assertItemsLeft(0);
    }

    @Test
    public void testEditAtAll(){
        page.givenAtAll(page.aTask("1", ACTIVE), page.aTask("2", COMPLETED));

        page.startEdit("2", "2 edited").pressEnter();
        page.assertVisibleTasks("1", "2 edited");
        page.assertItemsLeft(1);
    }

    @Test
    public void testCompleteAtAll(){
        page.givenAtAll(ACTIVE, "1");

        page.toggle("1");
        page.assertTasks("1");
        page.assertItemsLeft(0);
    }

    @Test
    public void testCompleteAllAtAll(){
        page.givenAtAll(ACTIVE, "1", "2");

        page.toggleAll();
        page.assertTasks("1", "2");
        page.assertItemsLeft(0);
    }

    @Test
    public void testClearCompletedAtAll(){
        page.givenAtAll(page.aTask("1", ACTIVE), page.aTask("2", COMPLETED));

        page.clearCompleted();
        page.assertTasks("1");
        page.assertItemsLeft(1);
    }

    @Test
    public void testReopenAtAll(){
        page.givenAtAll(page.aTask("1", ACTIVE), page.aTask("2", COMPLETED));

        page.toggle("2");
        page.assertTasks("1", "2");
        page.assertItemsLeft(2);
    }

    @Test
    public void testDeleteByEmptyAtAll() {
        page.givenAtAll(ACTIVE, "1");

        page.startEdit("1", "").pressEnter();
        page.assertNoTasks();
    }
}
