package com.pieceofquality1;

import com.codeborne.selenide.ElementsCollection;
import org.junit.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;

public class SmokeE2EToDoMVCTest {

    @Test
    public void testTasksFlow() {
        open("https://todomvc4tasj.herokuapp.com/");
        add("1", "2");
        edit("2", "2 edited");
        assertTasksAre("1", "2 edited");
        toggle("1");
        filter("Completed");
        toggleAll();
        delete("2 edited");
        filter("Active");
        assertTasksAre("");
        filter("All");
        clearCompleted();

    }

    private void edit(String taskOldText, String taskNewText) {
        tasks.find(exactText(taskOldText)).doubleClick();
        tasks.find(cssClass("editing")).$(".edit").setValue(taskNewText).pressEnter();
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

    private void filter(String taskText){$(By.linkText(taskText)).click();}
}


