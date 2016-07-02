package com.pieceofquality.pageobjects;

import com.pieceofquality.pageobjects.pages.ToDoMVCPage;
import org.junit.Test;

import static com.pieceofquality.pageobjects.pages.ToDoMVCPage.TaskType.ACTIVE;

/**
 * Created by piece on 19.06.2016.
 */
public class ToDoMVCActiveFilterTest extends BaseTest{

    ToDoMVCPage page = new ToDoMVCPage();

    @Test
    public void testAddAtActive(){
        page.givenAtActive(ACTIVE, "1");

        page.add("2");
        page.assertVisibleTasks("1", "2");
        page.assertItemsLeft(2);
    }
    @Test
    public void testDeleteAtActive(){
        page.givenAtActive(ACTIVE, "1");

        page.delete("1");
        page.assertNoVisibleTasks();
    }

    @Test
    public void testEditAtActive() {
        page.givenAtActive(ACTIVE, "1");

        page.startEdit("1", "1 edited").pressEnter();
        page.assertVisibleTasks("1 edited");
        page.assertItemsLeft(1);
    }

    @Test
    public void testCompleteAtActive(){
        page.givenAtActive(page.aTask("1", ACTIVE), page.aTask("2", ACTIVE));

        page.toggle("1");
        page.assertVisibleTasks("2");
        page.assertItemsLeft(1);
    }

    @Test
    public void testEditByPressTabAtActive() {
        page.givenAtActive(ACTIVE, "1");

        page.startEdit("1", "1 edited").pressTab();
        page.assertTasks("1 edited");
        page.assertItemsLeft(1);
    }
}
