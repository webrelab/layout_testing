package ru.webrelab.layout_testing.playwright;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.webrelab.layout_testing.Executor;
import ru.webrelab.layout_testing.enums.MeasuringType;
import ru.webrelab.layout_testing.enums.ScreenSize;
import ru.webrelab.layout_testing.repository.RawDataSet;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PlLayoutTestingTest {
    private Page page;

    @BeforeEach
    void before() {
        new PlConfigurator().configure();
        page = PlEnv.INSTANCE.getPage();
    }

    @Test
    void testLargePage() throws InterruptedException {
        page.navigate("https://preview.cruip.com/solid/");
        page.setViewportSize(ScreenSize.DESKTOP.getWidth(), ScreenSize.DESKTOP.getHeight());
        ElementHandle container = page.querySelector("//body");
        final List<RawDataSet> dataSetList = new ArrayList<>();
        dataSetList.add(new RawDataSet("All page", container, MeasuringType.ALL));
//        dataSetList.add(new RawDataSet("Follow us block", container.querySelector(".social-footer"), MeasuringType.ALL.name()));

        final Executor executorDesktop = new Executor(
                dataSetList,
                "full_page",
                "cruip; landing",
                "CHROME",
                container
        );
        executorDesktop.execute();
    }
}
