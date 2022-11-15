package ru.webrelab.layout_testing.ifaces;

/**
 * Интерфес для реализации enum с фиксированными размерами экрана размерами экрана
 */
public interface IScreenSize {
    /**
     * @return человекочитаемое название размера экрана для вывода в логах
     */
    String getDescription();

    /**
     * @return ширина вьюпорта (не окна браузера, а например тега body для веб-приложения)
     */
    int getWidth();

    /**
     * @return высота вьюпорта (не окна браузера, а например тега body для веб-приложения)
     */
    int getHeight();
}
