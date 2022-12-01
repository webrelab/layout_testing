package ru.webrelab.layout_testing.ifaces;

import ru.webrelab.layout_testing.repository.PositionRepository;
import ru.webrelab.layout_testing.repository.SizeRepository;
import ru.webrelab.layout_testing.screen_difference.DifferentElements;

import java.util.List;
import java.util.Map;

/**
 * The interface is designed to implement data storage classes for layout of various aspects.
 * In most cases, it is enough to inherit from the AttributeRepository class, which already
 * has a basic implementation of data comparison.
 */
public interface IRepository {

    /**
     * The method must return a map of the class fields with their values for the possibility of
     * subsequent automated comparison of the values of these fields.
     * @return map of field names and their values
     */
    Map<String, Object> getFieldMap();

    /**
     * The method must perform a comparison of data in two objects - the current one and the one passed
     * as a parameter. The result of the check is a list of identified discrepancies.
     * @param expected an object with the expected data. The current object must store the actual data received from the screen.
     * @return a list of identified discrepancies in the data. May be empty.
     */
    List<DifferentElements> compareWith(IRepository expected);

    /**
     * The method provides an opportunity to check the usefulness of the element found on the screen.
     * For example, if a text element with empty texts is received from the screen, then its usefulness is zero
     * @return false if the element is not testable
     */
    boolean check();

    /**
     * The method provides the ability to calculate the position of the element
     * @param container the position of the container relative to which the position of the element is calculated
     * @return PositionRepository object with element coordinates
     */
    PositionRepository getPosition(PositionRepository container, Object webElement);

    /**
     * The method provides the ability to calculate the size of the element
     * @return a SizeRepository object with the size of the element
     */
    SizeRepository getSize(Object webElement);
    String getTransform(Object webElement);
}
