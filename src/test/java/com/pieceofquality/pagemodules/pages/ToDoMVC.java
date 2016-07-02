package com.pieceofquality.pagemodules.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static com.pieceofquality.Helpers.doubleClick;

/**
 * Created by piece on 19.06.2016.
 */
public class ToDoMVC {

    public static ElementsCollection tasks = $$("#todo-list li");

    public static void add(String... taskTexts) {
        for (String text : taskTexts) {
            $("#new-todo").setValue(text).pressEnter();
        }
    }

    public static SelenideElement startEdit(String oldTaskText, String newTaskText) {
        doubleClick(tasks.find(exactText(oldTaskText)).find("label"));
        return tasks.find(cssClass("editing")).$(".edit").setValue(newTaskText);
    }

    public static void delete(String taskText) {
        tasks.find(exactText(taskText)).hover().$(".destroy").click();
    }

    public static void toggle(String taskText) {
        tasks.find(exactText(taskText)).$(".toggle").click();
    }

    public static void toggleAll() {
        $("#toggle-all").click();
    }

    public static void clearCompleted() {
        $("#clear-completed").click();
        $("#clear-completed").shouldNotBe(visible);
    }

    public static void filterAll() {
        $(By.linkText("All")).click();
    }

    public static void filterActive() {
        $(By.linkText("Active")).click();
    }

    public static void filterCompleted() {
        $(By.linkText("Completed")).click();
    }

    public static void assertTasks(String... taskTexts) {
        tasks.shouldHave(exactTexts(taskTexts));
    }

    public static void assertNoTasks() {
        tasks.shouldBe(empty);
    }

    public static void assertVisibleTasks(String... tasksTexts) {
        tasks.filter(visible).shouldHave(exactTexts(tasksTexts));
    }

    public static void assertNoVisibleTasks() {
        tasks.filter(visible).shouldBe(empty);
    }

    public static void assertItemsLeft(int count) {
        $("#todo-count>strong").shouldHave(exactText(Integer.toString(count)));
    }

    // pre-conditions

    public static void ensurePageOpened(){
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

    public static class Task {
        String taskText;
        TaskType taskType;

        public Task(String taskText, TaskType taskType) {
            this.taskText = taskText;
            this.taskType = taskType;
        }
    }

    public static void givenAtAll(Task... tasks) {
        given(tasks);
    }

    public static void givenAtActive(Task... tasks) {
        given(tasks);
        filterActive();
    }

    public static void givenAtCompleted(Task... tasks) {
        given(tasks);
        filterCompleted();
    }

    public static void givenAtAll(TaskType taskType, String... taskTexts) {
        Task[] tasks = new Task[taskTexts.length];
        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = new Task(taskTexts[i], taskType);
        }
        given(tasks);
    }

    public static void givenAtActive(TaskType taskType, String... taskTexts) {
        givenAtAll(taskType, taskTexts);
        filterActive();
    }

    public static void givenAtCompleted(TaskType taskType, String... taskTexts) {
        givenAtAll(taskType, taskTexts);
        filterCompleted();
    }

    public static void given(Task... tasks) {

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

    public static Task aTask(String taskText, TaskType taskType) {
        return new Task(taskText, taskType);
    }
}
