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
        startEdit("1", "1 edited").pressEnter();

        //complete
        toggle("1 edited");
        assertVisibleTasks("1 edited");

        filterCompleted();

        //reopen
        toggle("1 edited");
        assertNoVisibleTasks();

        filterActive();
        assertTasks("1 edited");
        add("2");
        assertTasks("1 edited", "2");

        //cancel
        startEdit("2", "2 edited").pressEscape();
        assertTasks("1 edited", "2");

        //delete
        delete("1 edited");
        assertTasks("2");

        //complete all
        toggleAll();
        assertNoVisibleTasks();


        filterAll();
        assertTasks("2");
        assertItemsLeft(0);
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