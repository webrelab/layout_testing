package ru.webrelab.layout_testing;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class LayoutTestingConfigurator {
    public void configure() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver_107");
        LayoutConfiguration.INSTANCE.setMethodsInjection(new MethodsInjectionImpl());

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        chromeOptions.setAcceptInsecureCerts(true);
        Environment.INSTANCE.driver = new ChromeDriver(chromeOptions);
        Environment.INSTANCE.driver.manage().window().maximize();
    }
}
