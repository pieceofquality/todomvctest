package com.pieceofquality;

import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
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
    public void testToDoMVCTaskFlow(){

        open("https://todomvc4tasj.herokuapp.com/");

        /*Создаем 4 задачи и проверяем их наличие*/
        $("#new-todo").setValue("Task1").pressEnter();
        $("#new-todo").setValue("Task2").pressEnter();
        $("#new-todo").setValue("Task3").pressEnter();
        $("#new-todo").setValue("Task4").pressEnter();
        $$("#todo-list li").shouldHave(exactTexts("Task1", "Task2", "Task3", "Task4"));

        /*Удаляем задачу №2, проверяем, что она отсутствует */
        $("#todo-list li[data-index='1']").hover();
        $("#todo-list li[data-index='1'] .destroy").click();
        $$("#todo-list li").shouldHave(exactTexts("Task1", "Task3", "Task4"));

        /*Отмечаем задачу №4 как выполненную. Проверяем, что она вычеркнута*/
        $("#todo-list li[data-index='3'] .toggle").click();
        $("#filters li .selected").click();


        /*Очищаем выполненные задачи (а это №4). Проверяем ее отсутствие*/
        $("#clear-completed").click();
        $$("#todo-list li").shouldHaveSize(2);

        /*Выделяем весь список задач и проверяем, что задач больше нет*/
        $("#toggle-all").click();
        $("#clear-completed").click();
        $$("#todo-list li").shouldBe(empty);

    }

}
