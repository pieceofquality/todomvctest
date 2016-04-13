package com.pieceofquality;

import com.codeborne.selenide.ElementsCollection;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;

/**
  1 create task1
  2 create task2
  3 create task3
  4 create task4
  5 delete task2
  6 mark task4 as completed
  7 clear completed
  8 mark all as completed
  9 clear completed

 */
public class ToDoMVCTest{

    @Test
    public void testTasksFlow(){
        open("https://todomvc4tasj.herokuapp.com/");

        add("1", "2", "3", "4");
        assertTasksAre("1", "2", "3", "4");

        delete("2");
        assertTasksAre("1", "3", "4");

        toggle("4");
        $("#clear-completed").click();

        assertTasksAre("1", "3");

        toggleAll();
        clearCompleted();
        tasks.shouldBe(empty);

    }

   ElementsCollection tasks = $$("#todo-list li");


   private void toggle(String taskText){
        tasks.find(exactText(taskText)).$(".toggle").click();
    }

   private void assertTasksAre(String... taskTexts){
        tasks.shouldHave(exactTexts(taskTexts));
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