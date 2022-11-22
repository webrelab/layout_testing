package ru.webrelab.layout_testing.selenium_web_driver;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import ru.webrelab.layout_testing.LayoutTestingException;
import ru.webrelab.layout_testing.ifaces.IMethodsInjection;
import ru.webrelab.layout_testing.repository.PositionRepository;
import ru.webrelab.layout_testing.repository.SizeRepository;
import ru.webrelab.layout_testing.screen_difference.DifferenceReport;
import ru.webrelab.layout_testing.utils.ScreenSizeUtils;

import java.util.List;

public class WdMethodsInjectionImpl implements IMethodsInjection {
    @Override
    public PositionRepository getPosition(PositionRepository container, Object object) {
        final Point point = ((WebElement) object).getLocation();
        return new PositionRepository(container, point.y, point.x);
    }

    @Override
    public Object executeJs(String js, Object... objects) {
        return ((RemoteWebDriver) WdEnv.INSTANCE.driver).executeScript(js, objects);
    }

    @Override
    public List<?> findElementsByXpath(Object object, String xpath) {
        return ((WebElement) object).findElements(By.xpath(xpath));
    }

    @Override
    public List<?> findElementsByXpath(String xpath) {
        return WdEnv.INSTANCE.driver.findElements(By.xpath(xpath));
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
        final Dimension dimension = WdEnv.INSTANCE.driver.manage().window().getSize();
        return new SizeRepository(dimension.height, dimension.width);
    }


    @Override
    public void setWindowSize(SizeRepository size) {
        WdEnv.INSTANCE.driver.manage().window().setSize(new Dimension(size.getWidth(), size.getHeight()));
    }

    @Override
    public void actionAfterTestFailed(List<DifferenceReport> reports) {
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        reports.forEach(System.out::println);
        throw new LayoutTestingException("Layout errors detected");
    }
}
