package com.pieceofquality;

import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.present;
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

        //Ability to create some tasks [Task1-4]
        $("#new-todo").setValue("Task1").pressEnter();
        $("#new-todo").setValue("Task2").pressEnter();
        $("#new-todo").setValue("Task3").pressEnter();
        $("#new-todo").setValue("Task4").pressEnter();
        $$("#todo-list li").shouldHave(exactTexts("Task1", "Task2", "Task3", "Task4"));

        //Deletion of any task [Task2]
        $("#todo-list li:nth-child(2)").hover();
        $("#todo-list li:nth-child(2) .destroy").click();
        $$("#todo-list li").shouldHave(exactTexts("Task1", "Task3", "Task4"));

        //Completion of task [Task4]
        $("#clear-completed").shouldBe(hidden);
        $("#todo-list li:nth-child(3) .toggle").click();
        $("#clear-completed").shouldBe(present);

        //Clearance of task [Task4]
        $("a[href='#/']").click();
        $("#clear-completed").click();
        $$("#todo-list li").shouldHave(exactTexts("Task1", "Task3"));

        // Batch clearance
        $("#toggle-all").click();
        $("#clear-completed").click();
        $$("#todo-list li").shouldBe(empty);

    }

}