package com.pieceofquality;

import com.codeborne.selenide.ElementsCollection;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

/**
 * Created by piece on 10.04.2016.
 */
public class SmokeE2ETest{

    @Test
    public void testTasksFlow(){
        open("https://todomvc4tasj.herokuapp.com/");

        add("1", "2", "3", "4");
        assertTasksAre("1", "2", "3", "4");

        delete("2");
        assertTasksAre("1", "3", "4");

        tasks.find(exactText("4")).$(".toggle").click();
        toggle("4");
        clearCompleted();
        assertTasksAre("1", "3");

        toggleAll();
        clearCompleted();
        tasks.shouldBe(empty);

    }

    ElementsCollection tasks = $$("#todo-list li");


    private void toggle(String text){
        tasks.find(exactText("text")).$(".toggle").click();
    }

    private void assertTasksAre(String... taskTexts){
        tasks.shouldHave(exactTexts("taskTexts"));
    }

    private void toggleAll(){
        $("#toggle-all").click();
    }

    private void clearCompleted() {
        $("#clear-completed").click();
    }

    private void add(String... taskTexts){
        for (String text: taskTexts) {
            $("#new-todo").setValue(text).pressEnter();
        }
    }

    private void delete(String taskText){
        tasks.find(exactText(taskText)).hover().$(".destroy").click();
    }

}