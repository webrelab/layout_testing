package ru.webrelab.layout_testing.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.webrelab.layout_testing.LayoutConfiguration;
import ru.webrelab.layout_testing.LayoutElement;
import ru.webrelab.layout_testing.repository.PositionRepository;
import ru.webrelab.layout_testing.screen_difference.DifferenceReport;
import ru.webrelab.layout_testing.snippets.Snippet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenDraw {
    private final PositionRepository container;

    public ScreenDraw(final PositionRepository container) {
        this.container = container;
        createContainer();
    }

    public void draw(final List<DifferenceReport> reports) {
        reports.forEach(this::draw);
    }

    private void draw(final DifferenceReport report) {
        if (report.getActual() != null) draw(CssClass.ACTUAL, report.getActual());
        if (report.getExpected() != null) draw(CssClass.EXPECTED, report.getExpected());
    }

    public void draw(final CssClass cssClass, final LayoutElement layoutElement) {
        final Map<String, Object> block = new HashMap<>();
        block.put("top", layoutElement.getPosition().getTop() + container.getTop());
        block.put("left", layoutElement.getPosition().getLeft() + container.getLeft());
        block.put("height", layoutElement.getSize().getHeight());
        block.put("width", layoutElement.getSize().getWidth());
        block.put("color", cssClass.className);
        block.put("transform", layoutElement.getTransform());

        LayoutConfiguration.INSTANCE
                .getFrameworkBasedBehavior().jsExecutor(Snippet.GREED_DRAW, block);
    }

    private void createContainer() {
        LayoutConfiguration.INSTANCE.getFrameworkBasedBehavior().jsExecutor(
                Map.of(
                        "ACTUAL_COLOR_VALUE", LayoutConfiguration.INSTANCE.getActualElementColor(),
                        "EXPECTED_COLOR_VALUE", LayoutConfiguration.INSTANCE.getExpectedElementColor()
                ),
                Snippet.CREATE_CONTAINER,
                null
        );
    }

    @RequiredArgsConstructor
    @Getter
    public enum CssClass {
        ACTUAL("actual_grid"),
        EXPECTED("expected_grid");
        private final String className;
    }
}
