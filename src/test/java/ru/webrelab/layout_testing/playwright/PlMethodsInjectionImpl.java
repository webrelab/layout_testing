package ru.webrelab.layout_testing.playwright;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.ScreenshotType;
import com.microsoft.playwright.options.ViewportSize;
import ru.webrelab.layout_testing.LayoutElement;
import ru.webrelab.layout_testing.LayoutTestingException;
import ru.webrelab.layout_testing.ifaces.IMethodsInjection;
import ru.webrelab.layout_testing.repository.PositionRepository;
import ru.webrelab.layout_testing.repository.SizeRepository;
import ru.webrelab.layout_testing.screen_difference.DifferenceReport;
import ru.webrelab.layout_testing.utils.ScreenDraw;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class PlMethodsInjectionImpl implements IMethodsInjection {
    @Override
    public PositionRepository getPosition(PositionRepository container, Object object) {
        final BoundingBox box = ((ElementHandle) object).boundingBox();
        if (box == null) return new PositionRepository(0, 0);
        return new PositionRepository(container, (int) box.y, (int) box.x);
    }

    @Override
    public Object executeJs(String js, Object... objects) {
        if (objects.length > 1) {
            return PlEnv.INSTANCE.getPage().evaluate(js, Arrays.asList(objects));
        }
        if (objects.length == 1) {
            return PlEnv.INSTANCE.getPage().evaluate(js, objects[0]);
        }
        return PlEnv.INSTANCE.getPage().evaluate(js);
    }

    @Override
    public List<?> findElementsByXpath(Object object, String xpath) {
        return ((ElementHandle) object).querySelectorAll("xpath=" + xpath);
    }

    @Override
    public List<?> findElementsByXpath(String xpath) {
        return PlEnv.INSTANCE.getPage().querySelectorAll("xpath=" + xpath);
    }

    @Override
    public String getText(Object object) {
        return ((ElementHandle) object).innerText();
    }

    @Override
    public String getTagName(Object object) {
        return ((ElementHandle) object).getProperty("tagName").toString();
    }

    @Override
    public String getAttributeValue(Object webElement, String attribute) {
        return ((ElementHandle) webElement).getAttribute(attribute);
    }

    @Override
    public SizeRepository getWindowBodySize() {
        ViewportSize viewportSize = PlEnv.INSTANCE.getPage().viewportSize();
        return new SizeRepository(viewportSize.height, viewportSize.width);
    }

    @Override
    public SizeRepository getWindowSize() {
        throw new LayoutTestingException("Use Playwright setViewportSize()");
    }

    @Override
    public void setWindowSize(SizeRepository size) {
        throw new LayoutTestingException("Use Playwright setViewportSize()");
    }

    @Override
    public void actionsBeforeTesting() {
        // do nothing
    }

    @Override
    public void actionAfterTestFailed(List<DifferenceReport> reports) {
        reports.stream().map(DifferenceReport::toString).forEach(System.out::println);
        reports.forEach(r -> {
            if (r.isElementNotFound()) {
                saveScreenshot(ScreenDraw.CssClass.EXPECTED, r.getExpected());
            } else {
                saveScreenshot(ScreenDraw.CssClass.ACTUAL, r.getActual());
            }
        });
        throw new LayoutTestingException("Layout errors detected");
    }

    private void saveScreenshot(final ScreenDraw.CssClass cssClass, final LayoutElement element) {
        final String id = cssClass.name() + "-" + element.getType().toString() + "-" + element.getId();
        final ElementHandle elementHandle = PlEnv.INSTANCE.getPage().querySelector(id);
        elementHandle.scrollIntoViewIfNeeded();
        final Path path = Paths.get("build", id + ".png");
        final Page.ScreenshotOptions options = new Page.ScreenshotOptions();
        options.path = path;
        PlEnv.INSTANCE.getPage().screenshot(options);
    }

    @Override
    public void actionAfterSnapshotCreated() {

    }
}
