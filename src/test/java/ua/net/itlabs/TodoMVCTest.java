package ua.net.itlabs;

import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selenide.*;

public class TodoMVCTest {

    @Test
    public void testCreateTask(){
        open("http://todomvc.com/examples/troopjs_require/#");
        $(".new-todo").setValue("Go hell").pressEnter();
        $(".new-todo").setValue("Go hell too").pressEnter();

        $$(".todo-list li").shouldHave(size(2));
        $$(".todo-list li").shouldHave(exactTexts("Go hell", "Go hell too"));
    }

}
