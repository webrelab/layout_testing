package ru.webrelab.layout_testing.playwright;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.webrelab.layout_testing.LayoutConfiguration;
import ru.webrelab.layout_testing.framework_based_behavior.PlaywrightFrameworkBehavior;
import ru.webrelab.layout_testing.selenium_web_driver.WdEnv;
import ru.webrelab.layout_testing.selenium_web_driver.WdMethodsInjectionImpl;

public class PlConfigurator {
    public void configure() {
        LayoutConfiguration.INSTANCE.setMethodsInjection(new PlMethodsInjectionImpl());
        LayoutConfiguration.INSTANCE.setFrameworkBasedBehavior(new PlaywrightFrameworkBehavior());
        LayoutConfiguration.INSTANCE.setTolerance(1);
    }
}
