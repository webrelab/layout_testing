package ru.webrelab.layout_testing;

import org.openqa.selenium.WebDriver;

public class Environment {
    public static final Environment INSTANCE = new Environment();
    private Environment() {}

    public WebDriver driver;
}
