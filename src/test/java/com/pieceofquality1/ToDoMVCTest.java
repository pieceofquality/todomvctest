package com.pieceofquality1;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ToDoMVCTest {

    @Test
    public void testTasksFlow() {

        open("https://todomvc4tasj.herokuapp.com/");

        add("1");
        assertTasks("1");
        toggle("1");
        assertVisibleTasks("1");

        filterCompleted();
        startEdit("1", "1 edited cancelled").pressEscape();
        assertTasks("1");
        //reopen
        toggle("1");
        assertNoVisibleTasks();
        assertItemsLeft(1);

        filterActive();
        startEdit("1", "1 edited").pressEnter();
        add("2");
        assertVisibleTasks("1 edited", "2");
        assertTasks("1 edited", "2");
        startEdit("2", "2 edited").pressTab();
        delete("2 edited");
        assertTasks("1 edited");

        filterAll();
        toggleAll();
        clearCompleted();
        assertNoTasks();
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

    private void delete(String taskTexts) {
        tasks.find(exactText(taskTexts)).hover().$(".destroy").click();
    }

    private void toggle(String taskTexts) {
        tasks.find(exactText(taskTexts)).$(".toggle").click();
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