package ru.webrelab.layout_testing.repository;

import lombok.Getter;

@Getter
public class PseudoAfterRepository extends PseudoElementsRepository{
    private final String content;
    private final String color;
    private final String background;

    public PseudoAfterRepository(final Object webElement) {
        super(webElement, "after");
        content = (String) getData().get("content");
        color = (String) getData().get("color");
        background = (String) getData().get("background");
    }
}
