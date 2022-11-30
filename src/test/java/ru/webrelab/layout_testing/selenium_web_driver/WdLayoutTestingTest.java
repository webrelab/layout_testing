package ru.webrelab.layout_testing.selenium_web_driver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import ru.webrelab.layout_testing.Executor;
import ru.webrelab.layout_testing.enums.MeasuringType;
import ru.webrelab.layout_testing.enums.ScreenSize;
import ru.webrelab.layout_testing.repository.RawDataSet;
import ru.webrelab.layout_testing.utils.ReadWriteUtils;
import ru.webrelab.layout_testing.utils.ScreenSizeUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class WdLayoutTestingTest {
    private WebDriver driver;

    @BeforeEach
    void before() throws IOException {
        Files.deleteIfExists(
                ReadWriteUtils.getPath(
                        "full_page",
                        "CHROME",
                        ScreenSize.FULL_HD,
                        "sienna",
                        "webdriver",
                        "landing"
                )
        );
        new WdConfigurator().configure();
        driver = WdEnv.INSTANCE.driver;
    }

    @AfterEach
    void after() {
        driver.close();
        driver.quit();
    }

    @Test
    public void testLargePage() throws InterruptedException {
        // create snapshot
        driver.get("file://" + System.getProperty("user.dir") + "/src/test/resources/data/html/sienna/index.html");
        ScreenSizeUtils.setWindowSize(ScreenSize.FULL_HD);
        driver.findElement(By.className("accordion-icon")).click();
        Thread.sleep(200);
        WebElement container = driver.findElement(By.tagName("body"));
        final List<RawDataSet> dataSetList = new ArrayList<>();
        dataSetList.add(new RawDataSet(
                "All page",
                container,
                MeasuringType.ALL
        ));
        Executor executorDesktop = new Executor(
                dataSetList,
                "full_page",
                "sienna; webdriver; landing",
                "CHROME",
                container
        );
        executorDesktop.execute();

        // check snapshot
        driver.navigate().refresh();
        driver.findElement(By.className("accordion-icon")).click();
        new Actions(driver).scrollByAmount(0, -10000).build().perform();
        container = driver.findElement(By.tagName("body"));
        dataSetList.clear();
        dataSetList.add(new RawDataSet(
                "All page",
                container,
                MeasuringType.ALL
        ));
        executorDesktop = new Executor(
                dataSetList,
                "full_page",
                "sienna; webdriver; landing",
                "CHROME",
                container
        );
        executorDesktop.execute();
    }
}
