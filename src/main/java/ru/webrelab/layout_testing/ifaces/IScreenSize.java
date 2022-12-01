package ru.webrelab.layout_testing.ifaces;

/**
 * Interface for implementing enum with fixed screen sizes
 */
public interface IScreenSize {
    /**
     * @return human-readable screen size name to display in logs
     */
    String getDescription();

    /**
     * @return viewport width (not the browser window, but for example the body tag for a web application)
     */
    int getWidth();

    /**
     * @return viewport height (not the browser window, but for example the body tag for a web application)
     */
    int getHeight();
}
