package ru.webrelab.layout_testing.selenium_web_driver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import ru.webrelab.layout_testing.Executor;
import ru.webrelab.layout_testing.enums.MeasuringType;
import ru.webrelab.layout_testing.enums.ScreenSize;
import ru.webrelab.layout_testing.repository.RawDataSet;
import ru.webrelab.layout_testing.utils.ScreenSizeUtils;

import java.util.ArrayList;
import java.util.List;

public class WdLayoutTestingTest {
    private WebDriver driver;

    @BeforeEach
    void before() {
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
//        driver.get("https://les.media/articles/650884-chego-dobivaetsya-rodina");
        driver.get("https://preview.cruip.com/solid/");
        Thread.sleep(1000);
        Actions actions = new Actions(driver);
        actions.scrollToElement(driver.findElement(By.className("site-footer"))).build().perform();
        actions.scrollByAmount(0, -10000).build().perform();
        WebElement container = driver.findElement(By.tagName("body"));
        final List<RawDataSet> dataSetList = new ArrayList<>();
        dataSetList.add(new RawDataSet("All page", container,
                MeasuringType.ALL
        ));
//        dataSetList.add(new RawDataSet("Follow us block", container.findElement(By.className("social-footer")), MeasuringType.ALL.name()));

        ScreenSizeUtils.setWindowSize(ScreenSize.FULL_HD);
        final Executor executorDesktop = new Executor(
                dataSetList,
                "full_page",
                "cruip; landing",
                "CHROME",
                container
        );
        executorDesktop.execute();

        Thread.sleep(10000);

    }

    @Test
    void svgElements() {
        driver.get("https://les.media/articles/650884-chego-dobivaetsya-rodina");
        WebElement container = driver.findElement(By.xpath("//body"));
        final List<RawDataSet> dataSetList = new ArrayList<>();
        dataSetList.add(new RawDataSet("Full page", container, MeasuringType.SVG.name()));

        ScreenSizeUtils.setWindowSize(ScreenSize.DESKTOP);
        final Executor executorDesktop = new Executor(
                dataSetList,
                "svg_testing",
                "articles; long",
                "CHROME", container
        );
        executorDesktop.execute();
    }
}
