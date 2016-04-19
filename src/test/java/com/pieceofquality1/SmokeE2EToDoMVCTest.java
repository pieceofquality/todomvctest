package com.pieceofquality1;

import com.codeborne.selenide.ElementsCollection;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;

public class SmokeE2EToDoMVCTest {

    @Test
    public void testTasksFlow() {

        open("https://todomvc4tasj.herokuapp.com/");

        //editing
        add("1", "2");
        edit("1", "1 edited");
        assertTasksAre("1 edited", "2");

        //complete
        toggle("1 edited");
        filterSwitchToActive();
        assertTasksAre("", "2");

        //reopen
        filterSwitchToCompleted();
        toggle("1 edited");
        filterSwitchToActive();
        assertTasksAre("1 edited", "2");


       //cancel edit
        filterSwitchToAll();
        cancelEdit("1 edited", "1");


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


    private void filterSwitchToAll() {
        $("[href='#/']").click();
    }

    private void filterSwitchToActive() {
        $("[href='#/active']").click();
    }

    private void filterSwitchToCompleted() {
        $("[href='#/completed']").click();
    }

    private void assertTasksEmpty() {
        tasks.shouldBe(empty);
    }

}
