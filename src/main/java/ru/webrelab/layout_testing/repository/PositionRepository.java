package ru.webrelab.layout_testing.repository;

import lombok.Getter;

@Getter
public class PositionRepository extends DimensionRepository {
    private final int top;
    private final int left;
    private final boolean isAbsolute;

    /**
     * Конструктор для задания абсолютной позиции элемента
     * @param top позиция от верхней линии страницы
     * @param left позиция от левой линии страницы
     */
    public PositionRepository(int top, int left) {
        this(top, left, true);
    }

    /**
     * Конструктор для задания позиции с указанием того, относительная она или абсолютная
     * @param top вертикальная позиция
     * @param left горизонтальная позиция
     * @param isAbsolute true если указанная позиция абсолютная (от верхней левой точки страницы)
     */
    public PositionRepository(int top, int left, boolean isAbsolute) {
        this.top = top;
        this.left = left;
        this.isAbsolute = isAbsolute;

    }

    /**
     * Конструктор для задания относительной позиции
     * @param container элемент страницы, относительно которого высчитывается позиция
     * @param top позиция от верхней линии страницы
     * @param left позиция от левой линии страницы
     */
    public PositionRepository(final PositionRepository container, final int top, final int left) {
        this.top = top - container.top;
        this.left = left - container.left;
        isAbsolute = false;
    }
}
