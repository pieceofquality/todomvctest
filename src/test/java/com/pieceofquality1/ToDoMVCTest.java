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
        edit("1", "1 edited").pressEnter();
        assertTasksAre("1 edited");

        //complete
        toggle("1 edited");
        assertVisible("1 edited");

        //reopen
        filterCompleted();
        toggle("1 edited");
        assertNoVisible();

//        cancel
        filterActive();
        assertTasksAre("1 edited");
        add("2");
        edit("2", "2 edited").pressEscape();
        assertTasksAre("1 edited", "2");

        //delete
        delete("1 edited");
        assertTasksAre("2");

        //complete all
        toggleAll();
        assertNoVisible();
        filterAll();
        clearCompleted();
        assertTasksEmpty();
    }

    private SelenideElement edit (String oldTaskText, String newTaskText) {
        tasks.find(exactText(oldTaskText)).doubleClick();
       return tasks.find(cssClass("editing")).$(".edit").setValue(newTaskText);

    }

    ElementsCollection tasks = $$("#todo-list li");


    private void toggle(String text) {
        tasks.find(exactText(text)).$(".toggle").click();
    }

    private void assertTasksAre(String... taskTexts) {
        tasks.shouldHave(exactTexts(taskTexts));
    }

    private void toggleAll() {
        $("#toggle-all").click();
    }

    private void clearCompleted() {
        $("#clear-completed").click();
    }

    private void add(String... taskTexts) {
        for (String text : taskTexts) {
            $("#new-todo").setValue(text).pressEnter();
        }
    }

    private void delete(String taskText) {
        tasks.find(exactText(taskText)).hover().$(".destroy").click();
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

    private void assertTasksEmpty() {
        tasks.shouldBe(empty);
    }

    private void assertVisible(String... tasksTexts) {
        tasks.filter(visible).shouldHave(exactTexts(tasksTexts));
    }
    private void assertNoVisible() {
        tasks.filter(visible).shouldBe(empty);
    }


}
