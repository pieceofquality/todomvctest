package com.pieceofquality1;

import com.codeborne.selenide.ElementsCollection;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class ToDoMVCTest {

    @Test
    public void testTasksFlow() {

        open("https://todomvc4tasj.herokuapp.com/");

        //editing
        add("1");
        edit("1", "1 edited");
        assertTasksAre("1 edited");

        //complete
        toggle("1 edited");
        assertVisible("1 edited");

        //reopen
        filterCompleted();
        toggle("1 edited");
        assertNoVisible();


       //cancel edit
        filterActive();
        add("2");
        cancelEdit("2", "2 edited");


        //delete
        delete("1 edited");
        assertTasksAre("2");

        //complete all
        toggleAll();
        clearCompleted();
        assertTasksEmpty();
    }

    private void edit(String taskOldText, String taskNewText) {
        tasks.find(exactText(taskOldText)).doubleClick();
        tasks.find(cssClass("editing")).$(".edit").setValue(taskNewText).pressEnter();
    }
    private void cancelEdit(String taskOldText,String taskNewText) {
        tasks.find(exactText(taskOldText)).doubleClick();
        tasks.find(cssClass("editing")).$(".edit").setValue(taskNewText).pressEscape();
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
        $("[href='#/']").click();
    }

    private void filterActive() {
        $("[href='#/active']").click();
    }

    private void filterCompleted() {
        $("[href='#/completed']").click();
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
