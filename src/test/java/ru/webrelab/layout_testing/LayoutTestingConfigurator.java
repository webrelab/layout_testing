package ru.webrelab.layout_testing;

import org.openqa.selenium.chrome.ChromeDriver;

public class LayoutTestingConfigurator {
    public void configure() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver_107");
        LayoutConfiguration.INSTANCE.setMethodsInjection(new MethodsInjectionImpl());
        Environment.INSTANCE.driver = new ChromeDriver();
    }
}
