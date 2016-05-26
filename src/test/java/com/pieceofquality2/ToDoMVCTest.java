package com.pieceofquality2;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ToDoMVCTest {

    @Before
    public void openSite(){
        open("https://todomvc4tasj.herokuapp.com");
    }

    @After
    public void clearData(){
        executeJavaScript("localStorage.clear()");
    }

//    @After
//    public void tearDown() throws IOException {
//        screenshot();
//    }
//
//    @Attachment(type = "image/png")
//    public byte[] screenshot() throws IOException {
//        File screenshot = Screenshots.getLastScreenshot();
//        return Files.toByteArray(screenshot);
//    }

    @Test
    public void testTasksFlow() {

        open("https://todomvc4tasj.herokuapp.com/");

        add("1");

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
    public void testEditAtActive(){
//      given - task added
        add("1");
        filterActive();

        startEdit("1", "1 edited").pressEnter();
        assertVisibleTasks("1 edited");
        assertItemsLeft(1);
    }

    @Test
    public void testCancelEditAtCompleted(){
//      given - task added and completed
        add("1");
        toggle("1");
        filterCompleted();

        startEdit("1", "1 cancelled").pressEscape();
        assertVisibleTasks("1");
        assertItemsLeft(0);
    }

    ElementsCollection tasks = $$("#todo-list li");

    private void add(String... taskTexts) {
        for (String text : taskTexts) {
            $("#new-todo").setValue(text).pressEnter();
        }
    }

    private SelenideElement startEdit(String oldTaskText, String newTaskText) {
        tasks.find(exactText(oldTaskText)).doubleClick();
        return tasks.find(cssClass("editing")).$(".edit").setValue(newTaskText);
    }

    private void delete(String taskText) {
        tasks.find(exactText(taskText)).hover().$(".destroy").click();
    }

    private void toggle(String taskText) {
        tasks.find(exactText(taskText)).$(".toggle").click();
    }

    private void toggleAll() {
        $("#toggle-all").click();
    }

    private void clearCompleted() {
        $("#clear-completed").click();
        $("#clear-completed").shouldNotBe(visible);
    }

    private void filterAll() {
        $(By.linkText("All")).click();
    }

    private void filterActive() {
        $(By.linkText("Active")).click();
    }

    private void filterCompleted() {
        $(By.linkText("Completed")).click();
    }

    private void assertTasks(String... taskTexts) {
        tasks.shouldHave(exactTexts(taskTexts));
    }

    private void assertNoTasks() {
        tasks.shouldBe(empty);
    }

    private void assertVisibleTasks(String... tasksTexts) {
        tasks.filter(visible).shouldHave(exactTexts(tasksTexts));
    }

    private void assertNoVisibleTasks() {
        tasks.filter(visible).shouldBe(empty);
    }

    private void assertItemsLeft(int count) {
        $("#todo-count>strong").shouldHave(exactText(Integer.toString(count)));
    }
}