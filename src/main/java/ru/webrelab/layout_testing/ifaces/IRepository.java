package ru.webrelab.layout_testing.ifaces;

import ru.webrelab.layout_testing.screen_difference.DifferentElements;

import java.util.List;
import java.util.Map;

public interface IRepository {
    Map<String, Object> getFieldMap();
    List<DifferentElements> compareWith(IRepository expected);
}
