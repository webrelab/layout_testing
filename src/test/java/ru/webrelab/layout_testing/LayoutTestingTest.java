package ru.webrelab.layout_testing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.webrelab.layout_testing.enums.MeasuringType;
import ru.webrelab.layout_testing.enums.ScreenSize;
import ru.webrelab.layout_testing.repository.RawDataSet;
import ru.webrelab.layout_testing.utils.ScreenSizeUtils;

import java.util.ArrayList;
import java.util.List;

public class LayoutTestingTest {
    private WebDriver driver;
    @BeforeEach
    void before() {
        new LayoutTestingConfigurator().configure();
        driver = Environment.INSTANCE.driver;
    }
    @AfterEach
    void after() {
        driver.close();
        driver.quit();
    }
    @Test
    public void startBrowser() {
        driver.get("https://les.media/articles/650884-chego-dobivaetsya-rodina");
        WebElement container = driver.findElement(By.xpath("//body"));
        final List<RawDataSet> dataSetList = new ArrayList<>();
        dataSetList.add(new RawDataSet("Full page", container, MeasuringType.ALL.name()));

        ScreenSizeUtils.setWindowSize(ScreenSize.DESKTOP);
        final Executor executorDesktop = new Executor(
                dataSetList,
                "full_page",
                "articles; long",
                "CHROME", container
        );
        executorDesktop.execute();

        ScreenSizeUtils.setWindowSize(ScreenSize.FULL_HD);
        driver.navigate().refresh();
        container = driver.findElement(By.xpath("//body"));
        dataSetList.clear();
        dataSetList.add(new RawDataSet("Full page", container, MeasuringType.ALL.name()));
        final Executor executorFullHd = new Executor(
                dataSetList,
                "full_page",
                "articles; long",
                "CHROME",
                container
        );
        executorFullHd.execute();

        ScreenSizeUtils.setWindowSize(ScreenSize.TABLET_LANDSCAPE);
        driver.navigate().refresh();
        container = driver.findElement(By.xpath("//body"));
        dataSetList.clear();
        dataSetList.add(new RawDataSet("Full page", container, MeasuringType.ALL.name()));
        final Executor executorTabletLandscape = new Executor(
                dataSetList,
                "full_page",
                "articles; long",
                "CHROME",
                container);
        executorTabletLandscape.execute();

        ScreenSizeUtils.setWindowSize(ScreenSize.TABLET_PORTRAIT);
        driver.navigate().refresh();
        container = driver.findElement(By.xpath("//body"));
        dataSetList.clear();
        dataSetList.add(new RawDataSet("Full page", container, MeasuringType.ALL.name()));
        final Executor executorTabletPortrait = new Executor(
                dataSetList,
                "full_page",
                "articles; long",
                "CHROME",
                container);
        executorTabletPortrait.execute();
    }
}
