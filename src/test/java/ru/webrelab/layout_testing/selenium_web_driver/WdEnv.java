package ru.webrelab.layout_testing.selenium_web_driver;

import org.openqa.selenium.WebDriver;

public class WdEnv {
    public static final WdEnv INSTANCE = new WdEnv();
    private WdEnv() {}

    public WebDriver driver;
}
