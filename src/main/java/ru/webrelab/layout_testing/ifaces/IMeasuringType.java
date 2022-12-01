package ru.webrelab.layout_testing.ifaces;

/**
 * Interface for enum implementation with layout testing options
 */
public interface IMeasuringType {
    /**
     * The method must return a specific xpath by which you can get all objects of the required type
     * @return xpath
     */
    String getXpath();

    /**
     * The method should return the color in which the grid over this type of element should be painted at the moment the snapshot is taken
     * @return color as #2e45af
     */
    String getColor();

    /**
     * Grid tilt angle for ease of perception of different types of measurements
     * @return numeric value from -90 to 90
     */
    int getDeg();

    /**
     * This method will get all the types included in the ALL (or COMPLEX) type
     * @return true if this type tests a particular aspect
     */
    boolean isComplex();

    /**
     * The method must return the repository class that is used to store this type of data
     * @return class of type IRepository
     */
    Class<? extends IRepository> getRepositoryClass();
}
