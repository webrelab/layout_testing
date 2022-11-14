package ru.webrelab.layout_testing.ifaces;

import ru.webrelab.layout_testing.screen_difference.DifferentElements;

import java.util.List;
import java.util.Map;

public interface IRepository {
    Map<String, Object> getFieldMap();
    List<DifferentElements> compareWith(IRepository expected);

    /**
     * Метод предоставляет возможность проверить полезность найденного на экране элемента
     * Например если с экрана получен элемент текстовый элемент с пустым текстов то полезность
     * его нулевая
     * @return false если элемент не пригоден для тестирования
     */
    boolean check();
}
