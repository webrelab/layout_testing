package ru.webrelab.layout_testing.playwright;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.webrelab.layout_testing.Executor;
import ru.webrelab.layout_testing.enums.MeasuringType;
import ru.webrelab.layout_testing.enums.ScreenSize;
import ru.webrelab.layout_testing.repository.RawDataSet;
import ru.webrelab.layout_testing.utils.ReadWriteUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class PlLayoutTestingTest {
    private Page page;

    @BeforeEach
    void before() throws IOException {
        Files.deleteIfExists(
                ReadWriteUtils.getPath(
                        "full_page",
                        "CHROME",
                        ScreenSize.FULL_HD,
                        "sienna",
                        "playwright",
                        "landing"
                )
        );
        new PlConfigurator().configure();
        page = PlEnv.INSTANCE.getPage();
    }

    @AfterEach
    void after() {
        page.close();
        PlEnv.INSTANCE.getBrowser().close();
    }

    @Test
    void testLargePage() throws InterruptedException {
        page.navigate("file://" + System.getProperty("user.dir") + "/src/test/resources/data/html/sienna/index.html");
        page.setViewportSize(ScreenSize.FULL_HD.getWidth(), ScreenSize.FULL_HD.getHeight());
        page.querySelector(".accordion-icon").click();
        ElementHandle container = page.querySelector("//body");
        final List<RawDataSet> dataSetList = new ArrayList<>();
        dataSetList.add(new RawDataSet(
                "All page",
                container,
//                MeasuringType.PSEUDO_BEFORE,
                MeasuringType.ALL
        ));

        Executor executorDesktop = new Executor(
                dataSetList,
                "full_page",
                "sienna; playwright; landing",
                "CHROME",
                container
        );
        executorDesktop.execute();

        page.reload();
        page.querySelector(".accordion-icon").click();
        page.querySelector(".site-header").scrollIntoViewIfNeeded();
        container = page.querySelector("//body");
        dataSetList.clear();
        dataSetList.add(new RawDataSet(
                "All page",
                container,
                MeasuringType.ALL
        ));
        executorDesktop = new Executor(
                dataSetList,
                "full_page",
                "sienna; playwright; landing",
                "CHROME",
                container
        );
        executorDesktop.execute();
    }
}
