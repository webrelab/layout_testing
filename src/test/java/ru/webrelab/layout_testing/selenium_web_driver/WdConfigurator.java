package ru.webrelab.layout_testing.selenium_web_driver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.webrelab.layout_testing.LayoutConfiguration;
import ru.webrelab.layout_testing.framework_based_behavior.SeleniumFrameworkBehavior;

public class WdConfigurator {
    public void configure() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver_107");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        chromeOptions.setAcceptInsecureCerts(true);
        WdEnv.INSTANCE.driver = new ChromeDriver(chromeOptions);
        LayoutConfiguration.INSTANCE.setMethodsInjection(new WdMethodsInjectionImpl());
        LayoutConfiguration.INSTANCE.setFrameworkBasedBehavior(new SeleniumFrameworkBehavior());
        WdEnv.INSTANCE.driver.manage().window().maximize();
    }
}
