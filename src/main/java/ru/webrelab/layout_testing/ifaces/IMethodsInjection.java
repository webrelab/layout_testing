package ru.webrelab.layout_testing.ifaces;

import ru.webrelab.layout_testing.repository.PositionRepository;
import ru.webrelab.layout_testing.repository.SizeRepository;
import ru.webrelab.layout_testing.screen_difference.DifferenceReport;

import java.util.List;

/**
 * Интерфейс предоставляет возможность использовать компонент тестирования верстки
 * на своём фреймворке, вне зависимости от используемой технологии доступа к браузеру
 */
public interface IMethodsInjection {

    /**
     * Метод требуется для получения текущей позиции элемента относительно
     * позиции контейнера
     * @param container объект PositionRepository с координатами контейнера, от которого должно
     *                  вычисляться положение веб-элемента
     * @param object веб-элемент
     * @return объект PositionRepository с текущими координатами элемента относительно контейнера
     */
    PositionRepository getPosition(PositionRepository container, Object object);

    /**
     * Метод требуется для исполнения Javascript кода на тестируемой странице.
     * @param js текст скрипта
     * @param objects список объектов, которые должны быть переданы в скрипт
     * @return результат выполнения скрипта
     */
    Object executeJs(String js, Object... objects);

    /**
     * Метод требуется для получения списка веб-элемент по xpath, находящихся
     * внутри переданного веб-элемента
     * @param object веб-элемент внутри которого производится поиск
     * @param xpath xpath для поиска
     * @return список найденных веб-элементов (может быть пустым)
     */
    List<?> findElementsByXpath(Object object, String xpath);

    /**
     * Метод требуется для получения списка веб-элемент по xpath, находящихся
     * на странице без привязки к другим элементам
     * @param xpath xpath для поиска
     * @return список найденных веб-элементов (может быть пустым)
     */
    List<?> findElementsByXpath(String xpath);

    /**
     * Метод требуется для получения текста из элемента
     * @param object веб-элемент
     * @return текст, полученный из веб-элемента
     */
    String getText(Object object);

    /**
     * Метод требуется для получения имени тега переданного в него элемента приложения
     * @param object веб-элемент
     * @return имя тега переданного элемента
     */
    String getTagName(Object object);

    /**
     * Метод требуется для получения значения атрибута веб-элемента
     * @param webElement веб-элемент
     * @param attribute название атрибута
     * @return значение атрибута
     */
    String getAttributeValue(Object webElement, String attribute);

    /**
     * Метод требуется для получения текущего размера вьюпорта (для браузера это
     * размер body)
     * @return объект SizeRepository с текущими размерами вьюпорта
     */
    SizeRepository getWindowBodySize();

    /**
     * Метод требуется для получения текущего размера окна приложения
     * @return объект SizeRepository с текущими размерами окна
     */
    SizeRepository getWindowSize();

    /**
     * Метод требуется для установки окна приложения заданного размера
     * @param size объект SizeRepository с заданными размерами окна
     */
    void setWindowSize(SizeRepository size);

    /**
     * Этот метод будет вызван в том случае, если при тестировании
     * верстки были выявлены ошибки. В метод передаётся список объектов,
     * содержащих ошибки
     * @param reports список объектов с ошибками
     */
    void actionAfterTestFailed(final List<DifferenceReport> reports);
}
