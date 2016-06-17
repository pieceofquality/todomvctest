package com.pieceofquality4;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static com.pieceofquality4.ToDoMVCTest.TaskType.ACTIVE;
import static com.pieceofquality4.ToDoMVCTest.TaskType.COMPLETED;

public class ToDoMVCTest extends BaseTest {

    @Test

    public void testTasksFlow() {

        givenAtAll(ACTIVE, "1");

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
    public void testAddAtAll(){
        given();
        add("1");
        assertTasks("1");
        assertItemsLeft(1);
    }

    @Test
    public void testDeleteAtAll(){
        givenAtAll(aTask("1", ACTIVE), aTask("2", COMPLETED));

        delete("1");
        assertTasks("2");
        assertItemsLeft(0);
    }

    @Test
    public void testEditAtAll(){
        givenAtAll(aTask("1", ACTIVE), aTask("2", COMPLETED));

        startEdit("2", "2 edited").pressEnter();
        assertVisibleTasks("1", "2 edited");
        assertItemsLeft(1);
    }

    @Test
    public void testCompleteAtAll(){
        givenAtAll(ACTIVE, "1");

        toggle("1");
        assertTasks("1");
        assertItemsLeft(0);
    }

    @Test
    public void testCompleteAllAtAll(){
        givenAtAll(ACTIVE, "1", "2");

        toggleAll();
        assertTasks("1", "2");
        assertItemsLeft(0);
    }

    @Test
    public void testClearCompletedAtAll(){
        givenAtAll(aTask("1", ACTIVE), aTask("2", COMPLETED));

        clearCompleted();
        assertTasks("1");
        assertItemsLeft(1);
    }

    @Test
    public void testReopenAtAll(){
        givenAtAll(aTask("1", ACTIVE), aTask("2", COMPLETED));

        toggle("2");
        assertTasks("1", "2");
        assertItemsLeft(2);
    }

    @Test
    public void testAddAtActive(){
        givenAtActive(ACTIVE, "1");

        add("2");
        assertVisibleTasks("1", "2");
        assertItemsLeft(2);
    }
    @Test
    public void testDeleteAtActive(){
        givenAtActive(ACTIVE, "1");

        delete("1");
        assertNoVisibleTasks();
    }

    @Test
    public void testEditAtActive() {
        givenAtActive(ACTIVE, "1");

        startEdit("1", "1 edited").pressEnter();
        assertVisibleTasks("1 edited");
        assertItemsLeft(1);
    }

    @Test
    public void testCompleteAtActive(){
        givenAtActive(aTask("1", ACTIVE), aTask("2", ACTIVE));

        toggle("1");
        assertVisibleTasks("2");
        assertItemsLeft(1);
    }

    @Test
    public void testEditAtCompleted() {

        givenAtCompleted(aTask("1", ACTIVE), aTask("2", COMPLETED));

        startEdit("2", "2 edited").pressEnter();
        assertVisibleTasks("2 edited");
        assertItemsLeft(1);
    }

    @Test
    public void testClearCompletedAllAtCompleted() {
        givenAtCompleted(aTask("1", COMPLETED), aTask("2", COMPLETED));

        toggle("1");
        clearCompleted();
        assertNoVisibleTasks();
        assertItemsLeft(1);
    }

    @Test
    public void testReopenAtCompleted(){
        givenAtCompleted(COMPLETED, "1");

        toggle("1");
        assertNoVisibleTasks();
        assertItemsLeft(1);
    }

    @Test
    public void testReopenAllAtCompleted(){
        givenAtCompleted(COMPLETED, "1", "2");

        toggleAll();
        assertNoVisibleTasks();
        assertItemsLeft(2);
    }

    @Test
    public void testCancelEditAtCompleted() {
        givenAtCompleted(COMPLETED, "1");

        startEdit("1", "1 cancelled").pressEscape();
        assertVisibleTasks("1");
        assertItemsLeft(0);
    }

    @Test
    public void testDeleteByEmptyAtAll() {
        givenAtAll(ACTIVE, "1");

        startEdit("1", "").pressEnter();
        assertNoTasks();
    }

    @Test
    public void testEditByPressTabAtActive() {
        givenAtActive(ACTIVE, "1");

        startEdit("1", "1 edited").pressTab();
        assertTasks("1 edited");
        assertItemsLeft(1);
    }

    @Test
    public void testEditByClickOutsideAtCompleted() {
        givenAtCompleted(COMPLETED, "1");

        startEdit("1", "1 edited");
        $("#new-todo").click();
        assertTasks("1 edited");
        assertItemsLeft(0);
    }

    @Test
    public void testFilterFromActiveToAll(){
        givenAtActive(aTask("1", ACTIVE), aTask("2", COMPLETED), aTask("3", ACTIVE), aTask("4", ACTIVE), aTask("5", COMPLETED));

        filterAll();
        assertTasks("1", "2", "3", "4", "5");
        assertItemsLeft(3);
    }

    @Test
    public void testFilterFromCompletedToActive() {
        givenAtCompleted(aTask("1", ACTIVE), aTask("2", COMPLETED), aTask("3", ACTIVE));

        filterActive();
        assertVisibleTasks("1", "3");
        assertItemsLeft(2);
    }

    @Test
    public void testFilterFromAllToCompleted() {
        givenAtAll(aTask("1", ACTIVE), aTask("2", COMPLETED), aTask("3", ACTIVE));

        filterCompleted();
        assertVisibleTasks("2");
        assertItemsLeft(2);
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

    private void ensurePageOpened(){
        if (! url().equals("https://todomvc4tasj.herokuapp.com/")) {
            open("https://todomvc4tasj.herokuapp.com/");
        }
    }
    public static enum TaskType {
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