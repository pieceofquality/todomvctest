package com.pieceofquality5.pageobjects;

import com.codeborne.selenide.Screenshots;
import com.google.common.io.Files;
import org.junit.After;
import ru.yandex.qatools.allure.annotations.Attachment;

import java.io.File;
import java.io.IOException;


public class BaseTest {

    @After
    public void tearDown() throws IOException {
        screenshot();
    }

    @Attachment
    public byte[] screenshot() throws IOException {
        File screenshot = Screenshots.takeScreenShotAsFile();
        return Files.toByteArray(screenshot);
    }
}
