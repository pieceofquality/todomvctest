package com.pieceofquality.pageobjects.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.refresh;
import static com.codeborne.selenide.WebDriverRunner.url;
import static com.pieceofquality.Helpers.*;

/**
 * Created by piece on 19.06.2016.
 */
public  class ToDoMVCPage {

    ElementsCollection tasks = $$("#todo-list li");

    public void add(String... taskTexts) {
        for (String text : taskTexts) {
            $("#new-todo").setValue(text).pressEnter();
        }
    }

    public SelenideElement startEdit(String oldTaskText, String newTaskText) {
        doubleClick(tasks.find(exactText(oldTaskText)).find("label"));
        return tasks.find(cssClass("editing")).$(".edit").setValue(newTaskText);
    }

    public void delete(String taskText) {
        tasks.find(exactText(taskText)).hover();
        tasks.find(exactText(taskText)).$(".destroy").click();
    }

    public void toggle(String taskText) {
        tasks.find(exactText(taskText)).$(".toggle").click();
    }

    public void toggleAll() {
        $("#toggle-all").click();
    }

    public void clearCompleted() {
        $("#clear-completed").click();
        $("#clear-completed").shouldNotBe(visible);
    }

    public void filterAll() {
        $(By.linkText("All")).click();
    }

    public void filterActive() {
        $(By.linkText("Active")).click();
    }

    public void filterCompleted() {
        $(By.linkText("Completed")).click();
    }

    public void assertTasks(String... taskTexts) {
        tasks.shouldHave(exactTexts(taskTexts));
    }

    public void assertNoTasks() {
        tasks.shouldBe(empty);
    }

    public void assertVisibleTasks(String... tasksTexts) {
        tasks.filter(visible).shouldHave(exactTexts(tasksTexts));
    }

    public void assertNoVisibleTasks() {
        tasks.filter(visible).shouldBe(empty);
    }

    public void assertItemsLeft(int count) {
        $("#todo-count>strong").shouldHave(exactText(Integer.toString(count)));
    }

    // pre-conditions

    public void ensurePageOpened(){
        if (! url().equals("https://todomvc4tasj.herokuapp.com/")) {
            open("https://todomvc4tasj.herokuapp.com/");
        }
    }
    public static enum TaskType {
        ACTIVE("false"),
        COMPLETED("true");

        public final String status;

        TaskType(String status) {
            this.status = status;
        }

        public String getType() {
            return status;
        }
    }

    public class Task {
        String taskText;
        TaskType taskType;

        public Task(String taskText, TaskType taskType) {
            this.taskText = taskText;
            this.taskType = taskType;
        }
    }

    public void givenAtAll(Task... tasks) {
        given(tasks);
    }

    public void givenAtActive(Task... tasks) {
        given(tasks);
        filterActive();
    }

    public void givenAtCompleted(Task... tasks) {
        given(tasks);
        filterCompleted();
    }

    public void givenAtAll(TaskType taskType, String... taskTexts) {
        Task[] tasks = new Task[taskTexts.length];
        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = new Task(taskTexts[i], taskType);
        }
        given(tasks);
    }

    public void givenAtActive(TaskType taskType, String... taskTexts) {
        givenAtAll(taskType, taskTexts);
        filterActive();
    }

    public void givenAtCompleted(TaskType taskType, String... taskTexts) {
        givenAtAll(taskType, taskTexts);
        filterCompleted();
    }

    public void given(Task... tasks) {

        ensurePageOpened();
        String elements = "localStorage.setItem('todos-troopjs', '[";
        for (Task task : tasks) {
            elements += "{\"completed\":" + task.taskType.getType() + ", \"title\":\"" + task.taskText + "\"},";
        }
        if (tasks.length > 0) {
            elements = elements.substring(0, (elements.length() - 1));
        }
        elements += "]');";
        executeJavaScript(elements);
        refresh();
    }

    public Task aTask(String taskText, TaskType taskType) {
        return new Task(taskText, taskType);
    }
}
