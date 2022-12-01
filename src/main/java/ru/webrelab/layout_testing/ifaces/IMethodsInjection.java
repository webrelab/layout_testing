package ru.webrelab.layout_testing.ifaces;

import ru.webrelab.layout_testing.repository.PositionRepository;
import ru.webrelab.layout_testing.repository.SizeRepository;
import ru.webrelab.layout_testing.screen_difference.DifferenceReport;

import java.util.List;

/**
 * The interface provides the ability to use the layout testing component on your own framework,
 * regardless of the browser access technology used
 */
public interface IMethodsInjection {

    /**
     * The method is required to get the current position of the element relative to the position of the container
     * @param container the PositionRepository object with the coordinates of the container from which the
     *                  position of the web element should be calculated
     * @param object web element
     * @return a PositionRepository object with the element's current coordinates relative to the container
     */
    PositionRepository getPosition(PositionRepository container, Object object);

    /**
     * The method is required to execute Javascript code on the page being tested.
     * @param js script text
     * @param objects list of objects to be passed to the script
     * @return script execution result
     */
    Object executeJs(String js, Object... objects);

    /**
     * The method is required to get a list of web elements by xpath that are inside the passed web element
     * @param object web element within which to search
     * @param xpath xpath to search
     * @return list of found web elements (may be empty)
     */
    List<?> findElementsByXpath(Object object, String xpath);

    /**
     * The method is required to get a list of web elements by xpath located on the page without reference to other elements
     * @param xpath xpath to search
     * @return list of found web elements (may be empty)
     */
    List<?> findElementsByXpath(String xpath);

    /**
     * The method is required to get the text from the element
     * @param object web element
     * @return text retrieved from the web element
     */
    String getText(Object object);

    /**
     * The method is required to get the name of the tag of the application element passed to it
     * @param object web element
     * @return tag name of passed element
     */
    String getTagName(Object object);

    /**
     * The method is required to get the value of an attribute of a web element
     * @param webElement web element
     * @param attribute attribute name
     * @return attribute value
     */
    String getAttributeValue(Object webElement, String attribute);

    /**
     * The method is required to get the current size of the viewport (for the browser, this is the size of the body)
     * @return SizeRepository object with current viewport sizes
     */
    SizeRepository getWindowBodySize();

    /**
     * The method is required to get the current size of the application window
     * @return a SizeRepository object with the current dimensions of the window
     */
    SizeRepository getWindowSize();

    /**
     * The method is required to set the application window to a given size
     * @param size a SizeRepository object with the specified window sizes
     */
    void setWindowSize(SizeRepository size);

    /**
     * This method will be called before layout testing starts. In it, you can implement any additional
     * preparatory actions. For example, execute JS code to prepare the page.
     */
    void actionsBeforeTesting();

    /**
     * This method will be called if errors were found during layout testing.
     * The list of objects containing errors is passed to the method
     * @param reports list of objects with errors
     */
    void actionAfterTestFailed(final List<DifferenceReport> reports);

    /**
     * This method will be called after creating a new snapshot
     */
    void actionAfterSnapshotCreated();
}
