package ru.webrelab.layout_testing.ifaces;

import ru.webrelab.layout_testing.repository.PositionRepository;
import ru.webrelab.layout_testing.repository.SizeRepository;
import ru.webrelab.layout_testing.screen_difference.DifferentElements;

import java.util.List;
import java.util.Map;

/**
 * Интерфейс предназначен для реализации классов хранения данных верски
 * разных аспектов.
 * В большинстве случаев достаточно наследоваться от класса AttributeRepository,
 * в котором уже есть базовая реализация сравнения даных.
 */
public interface IRepository {

    /**
     * Метод должен возвращать мапу из полей класса с их значениями
     * для возможности последующего автоматизированного сравнения
     * значений этих полей.
     * @return мапа из названий полей и их значений
     */
    Map<String, Object> getFieldMap();

    /**
     * Метод должен выполнять сравнение данных в двух объектах - текущем и переданным
     * в качестве параметра. Результат проверки - список из выявленных расхождений.
     * @param expected объект с ожидаемыми данными. Текущий объект должен хранить актуальные
     *                 данные, полученные с экрана.
     * @return список из выявленных расхождений в данных. Может быть пустым.
     */
    List<DifferentElements> compareWith(IRepository expected);

    /**
     * Метод предоставляет возможность проверить полезность найденного на экране элемента
     * Например если с экрана получен элемент текстовый элемент с пустым текстов то полезность
     * его нулевая
     * @return false если элемент не пригоден для тестирования
     */
    boolean check();

    /**
     * Метод предоставляет возможность рассчитать позицию элемента
     * @param container позиция контейнера относительно которой рассчитывается позиция элемента
     * @return объект PositionRepository с координатами элемента
     */
    PositionRepository getPosition(PositionRepository container, Object webElement);

    /**
     * Метод предоставляет возможность рассчитать размеры элемента
     * @return объект SizeRepository с размером элемента
     */
    SizeRepository getSize(Object webElement);
    String getTransform(Object webElement);
}
