package ru.webrelab.layout_testing.ifaces;

import ru.webrelab.layout_testing.repository.PositionRepository;
import ru.webrelab.layout_testing.repository.SizeRepository;

import java.util.List;

public interface IMethodsInjection {
    PositionRepository getPosition(PositionRepository container, Object object);
    SizeRepository getSize(Object object);
    Object executeJs(String js, Object... objects);

    List<?> findElementsByXpath(Object object, String xpath);

    String getText(Object object);

    String getTagName(Object object);
    SizeRepository getWindowBodySize();
    SizeRepository getWindowSize();
    void setWindowSize(SizeRepository size);
}
