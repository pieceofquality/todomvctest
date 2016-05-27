package com.pieceofquality3;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.Test;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ToDoMVCTest extends AtTodoMVCPageWithClearedDataAfterEachTest {

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

    @Step
    private void add(String... taskTexts) {
        for (String text : taskTexts) {
            $("#new-todo").setValue(text).pressEnter();
        }
    }

    @Step
    private SelenideElement startEdit(String oldTaskText, String newTaskText) {
        tasks.find(exactText(oldTaskText)).doubleClick();
        return tasks.find(cssClass("editing")).$(".edit").setValue(newTaskText);
    }

    @Step
    private void delete(String taskText) {
        tasks.find(exactText(taskText)).hover().$(".destroy").click();
    }

    @Step
    private void toggle(String taskText) {
        tasks.find(exactText(taskText)).$(".toggle").click();
    }

    @Step
    private void toggleAll() {
        $("#toggle-all").click();
    }

    @Step
    private void clearCompleted() {
        $("#clear-completed").click();
        $("#clear-completed").shouldNotBe(visible);
    }

    @Step
    private void filterAll() {
        $(By.linkText("All")).click();
    }

    @Step
    private void filterActive() {
        $(By.linkText("Active")).click();
    }

    @Step
    private void filterCompleted() {
        $(By.linkText("Completed")).click();
    }

    @Step
    private void assertTasks(String... taskTexts) {
        tasks.shouldHave(exactTexts(taskTexts));
    }

    @Step
    private void assertNoTasks() {
        tasks.shouldBe(empty);
    }

    @Step
    private void assertVisibleTasks(String... tasksTexts) {
        tasks.filter(visible).shouldHave(exactTexts(tasksTexts));
    }

    @Step
    private void assertNoVisibleTasks() {
        tasks.filter(visible).shouldBe(empty);
    }

    @Step
    private void assertItemsLeft(int count) {
        $("#todo-count>strong").shouldHave(exactText(Integer.toString(count)));
    }
}