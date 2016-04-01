package ua.net.itlabs;

import com.codeborne.selenide.Condition;
import org.junit.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class Google {

    @Test
    public void googleSearch(){
        open("http://google.com/ncr");
        $(By.name("q")).setValue("selenide").pressEnter();
        $$("#ires .g").shouldHave(size(10));
        $("#ires .g").shouldBe(visible).shouldHave(
                text("Selenide: concise UI tests in Java"),
                text("selenide.org")
        );
    }

}
