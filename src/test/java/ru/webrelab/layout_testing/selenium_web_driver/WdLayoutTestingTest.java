package ru.webrelab.layout_testing.selenium_web_driver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
    public void testLargePage() {
//        driver.get("https://les.media/articles/650884-chego-dobivaetsya-rodina");
        driver.get("https://www.jetbrains.com/aqua/");
        WebElement container = driver.findElement(By.className("page__content"));
        final List<RawDataSet> dataSetList = new ArrayList<>();
        dataSetList.add(new RawDataSet("Develop block", container.findElement(By.id("develop")), MeasuringType.ALL.name()));
        dataSetList.add(new RawDataSet("Follow us block", container.findElement(By.className("social-footer")), MeasuringType.ALL.name()));

        ScreenSizeUtils.setWindowSize(ScreenSize.DESKTOP);
        final Executor executorDesktop = new Executor(
                dataSetList,
                "container_fragment",
                "jetbrains; aqua",
                "CHROME",
                container
        );
        executorDesktop.execute();


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
