package ru.webrelab.layout_testing.ifaces;

/**
 * Интерфейс для имплементации enum с вариантами тестирования верстки
 */
public interface IMeasuringType {
    /**
     * Метод должен возвращать специфичный xpath по которому
     * можно получить все объекты необходимого типа
     * @return xpath
     */
    String getXpath();

    /**
     * Метод должен вернуть цвет, в который должна быть покрашена сеточка над этим типом элемента
     * в момент снятия снапшота
     * @return цвет в виде #2e45af
     */
    String getColor();

    /**
     * Угол наклона сетки для удобства восприятия разных типов измерений
     * @return числовое значение от -90 до 90
     */
    int getDeg();

    /**
     * По этому методу будут получены все типы, включенные в тип ALL (или COMPLEX)
     * @return true если этот тип тестирует конкретный аспект
     */
    boolean isComplex();

    /**
     * Метод должен вернуть класс репозитория, который используется для хранения
     * данных этого типа
     * @return класс типа IRepository
     */
    Class<? extends IRepository> getRepositoryClass();
}
