package ru.webrelab.layout_testing.selenium_web_driver;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import ru.webrelab.layout_testing.LayoutElement;
import ru.webrelab.layout_testing.LayoutTestingException;
import ru.webrelab.layout_testing.ifaces.IMethodsInjection;
import ru.webrelab.layout_testing.playwright.PlEnv;
import ru.webrelab.layout_testing.repository.PositionRepository;
import ru.webrelab.layout_testing.repository.SizeRepository;
import ru.webrelab.layout_testing.screen_difference.DifferenceReport;
import ru.webrelab.layout_testing.utils.ScreenDraw;
import ru.webrelab.layout_testing.utils.ScreenSizeUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class WdMethodsInjectionImpl implements IMethodsInjection {

    private final WebDriver driver = WdEnv.INSTANCE.driver;

    @Override
    public PositionRepository getPosition(PositionRepository container, Object object) {
        final Point point = ((WebElement) object).getLocation();
        return new PositionRepository(container, point.y, point.x);
    }

    @Override
    public Object executeJs(String js, Object... objects) {
        return ((RemoteWebDriver) driver).executeScript(js, objects);
    }

    @Override
    public List<?> findElementsByXpath(Object object, String xpath) {
        return ((WebElement) object).findElements(By.xpath(xpath));
    }

    @Override
    public List<?> findElementsByXpath(String xpath) {
        return driver.findElements(By.xpath(xpath));
    }

    @Override
    public String getText(Object object) {
        return ((WebElement) object).getText();
    }

    @Override
    public String getTagName(Object object) {
        return ((WebElement) object).getTagName();
    }

    @Override
    public String getAttributeValue(Object webElement, String attribute) {
        return ((WebElement) webElement).getAttribute(attribute);
    }

    @Override
    public SizeRepository getWindowBodySize() {
        return ScreenSizeUtils.getViewportSize();
    }

    @Override
    public SizeRepository getWindowSize() {
        final Dimension dimension = driver.manage().window().getSize();
        return new SizeRepository(dimension.height, dimension.width);
    }


    @Override
    public void setWindowSize(SizeRepository size) {
        driver.manage().window().setSize(new Dimension(size.getWidth(), size.getHeight()));
    }

    @Override
    public void actionsBeforeTesting() {
        // do nothing
    }

    @Override
    public void actionAfterTestFailed(List<DifferenceReport> reports) {
        reports.forEach(System.out::println);
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
        final WebElement webElement = driver.findElement(By.id(id));
        final Actions actions = new Actions(driver);
        actions.scrollToElement(webElement).build().perform();
        final byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        final Path path = Paths.get("build", id + ".png");
        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actionAfterSnapshotCreated() {

    }
}
