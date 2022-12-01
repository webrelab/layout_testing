package ru.webrelab.layout_testing.repository;

import lombok.Getter;

@Getter
public class PositionRepository extends DimensionRepository {
    private final int top;
    private final int left;
    private transient final boolean isAbsolute;

    /**
     * Constructor for setting the absolute position of an element
     * @param top position from the top of the page
     * @param left position from left page line
     */
    public PositionRepository(int top, int left) {
        this(top, left, true);
    }

    /**
     * Constructor for specifying a position indicating whether it is relative or absolute
     * @param top vertical position
     * @param left horizontal position
     * @param isAbsolute true if the specified position is absolute (from the top left of the page)
     */
    public PositionRepository(int top, int left, boolean isAbsolute) {
        this.top = top;
        this.left = left;
        this.isAbsolute = isAbsolute;

    }

    /**
     * Constructor for setting relative position
     * @param container page element relative to which the position is calculated
     * @param top position from the top of the page
     * @param left position from left page line
     */
    public PositionRepository(final PositionRepository container, final int top, final int left) {
        this.top = container == null ? top : top - container.top;
        this.left = container == null ? left : left - container.left;
        isAbsolute = false;
    }
}
