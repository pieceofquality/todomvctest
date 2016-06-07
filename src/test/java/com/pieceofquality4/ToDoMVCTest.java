package com.pieceofquality4;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.pieceofquality3.AtTodoMVCPageWithClearedDataAfterEachTest;
import org.junit.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;

public class ToDoMVCTest extends AtTodoMVCPageWithClearedDataAfterEachTest {

    @Test
    public void testTasksFlow() {

        open("https://todomvc4tasj.herokuapp.com/");

        add("1");

        toggle("1");
        assertTasks("1");

        filterActive();
        assertNoVisibleTasks();

        filterCompleted();
        assertVisibleTasks("1");

        //reopen
        toggle("1");
        assertNoVisibleTasks();
        assertItemsLeft(1);

        filterAll();
        assertTasks("1");

        toggleAll();

        clearCompleted();
        assertNoTasks();
    }

    @Test
    public void testEditAtActive() {
        givenAtActive(TaskType.ACTIVE, "1");
        startEdit("1", "1 edited").pressEnter();
        assertVisibleTasks("1 edited");
        assertItemsLeft(1);
    }

    @Test
    public void testCancelEditAtCompleted() {
        givenAtCompleted(TaskType.COMPLETED, "1");
        startEdit("1", "1 cancelled").pressEscape();
        assertVisibleTasks("1");
        assertItemsLeft(0);
    }

    ElementsCollection tasks = $$("#todo-list li");

    private void add(String... taskTexts) {
        for (String text : taskTexts) {
            $("#new-todo").setValue(text).pressEnter();
        }
    }

    private SelenideElement startEdit(String oldTaskText, String newTaskText) {
        tasks.find(exactText(oldTaskText)).doubleClick();
        return tasks.find(cssClass("editing")).$(".edit").setValue(newTaskText);
    }

    private void delete(String taskText) {
        tasks.find(exactText(taskText)).hover().$(".destroy").click();
    }

    private void toggle(String taskText) {
        tasks.find(exactText(taskText)).$(".toggle").click();
    }

    private void toggleAll() {
        $("#toggle-all").click();
    }

    private void clearCompleted() {
        $("#clear-completed").click();
        $("#clear-completed").shouldNotBe(visible);
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

    private void assertTasks(String... taskTexts) {
        tasks.shouldHave(exactTexts(taskTexts));
    }

    private void assertNoTasks() {
        tasks.shouldBe(empty);
    }

    private void assertVisibleTasks(String... tasksTexts) {
        tasks.filter(visible).shouldHave(exactTexts(tasksTexts));
    }

    private void assertNoVisibleTasks() {
        tasks.filter(visible).shouldBe(empty);
    }

    private void assertItemsLeft(int count) {
        $("#todo-count>strong").shouldHave(exactText(Integer.toString(count)));
    }

    // pre-conditions

    private void isPageOpened(){
        if (! url().equals("https://todomvc4tasj.herokuapp.com/")) {
            open("https://todomvc4tasj.herokuapp.com/");
        }
    }
    public enum TaskType {
        ACTIVE("false"),
        COMPLETED("true");

        private final String status;

        TaskType(String status) {
            this.status = status;
        }

        public String getType() {
            return status;
        }
    }

    public class Task {
        String taskName;
        TaskType taskType;

        public Task(String taskName, TaskType taskType) {
            this.taskName = taskName;
            this.taskType = taskType;
        }

        public String toString() {
            return "{\\\"completed\\\":" + taskType+ ", \\\"title\\\":\\\"" + taskName + "\\\"}, ";
        }
    }

    public Task aTask(String taskText, TaskType taskType) {
        return new Task(taskText, taskType);
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

        isPageOpened();
        String elements = "";
        for (Task task : tasks) {
            elements += "{\"completed\":" + task.taskType.getType() + ", \"title\":\"" + task.taskName + "\"},";
        }
        if (tasks.length > 0) {
            elements = elements.substring(0, (elements.length() - 1));
        }
        elements += "]');";
        executeJavaScript(elements);
        refresh();
    }
}