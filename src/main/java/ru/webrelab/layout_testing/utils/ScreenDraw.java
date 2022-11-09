package ru.webrelab.layout_testing.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.webrelab.layout_testing.LayoutConfiguration;
import ru.webrelab.layout_testing.LayoutElement;
import ru.webrelab.layout_testing.repository.PositionRepository;
import ru.webrelab.layout_testing.screen_difference.DifferenceReport;
import ru.webrelab.layout_testing.snippets.Snippet;
import ru.webrelab.layout_testing.snippets.SnippetsRepository;

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
        if (report.getActual() != null) draw(Color.ACTUAL, report.getActual());
        if (report.getExpected() != null) draw(Color.EXPECTED, report.getExpected());
    }

    private void draw(final Color color, final LayoutElement layoutElement) {
        final Map<String, Object> block = new HashMap<>();
        block.put("top", layoutElement.getPosition().getTop() + container.getTop());
        block.put("left", layoutElement.getPosition().getLeft() + container.getLeft());
        block.put("height", layoutElement.getSize().getHeight());
        block.put("width", layoutElement.getSize().getWidth());
        block.put("color", color.colorClass);

        LayoutConfiguration.INSTANCE.getMethods().executeJs(
                SnippetsRepository.INSTANCE.getSnippet(
                        Snippet.GREED_DRAW.getSnippet()
                        ),
                block
        );
    }

    private void createContainer() {
        LayoutConfiguration.INSTANCE.getMethods().executeJs(
                SnippetsRepository.INSTANCE.getSnippet(Snippet.CREATE_CONTAINER.getSnippet())
                        .replace("ACTUAL_COLOR_VALUE", LayoutConfiguration.INSTANCE.getActualElementColor())
                        .replace("EXPECTED_COLOR_VALUE", LayoutConfiguration.INSTANCE.getExpectedElementColor())
        );
    }

    @RequiredArgsConstructor
    @Getter
    private enum Color {
        ACTUAL(".actual_grid"),
        EXPECTED(".expected_grid")
        ;
        private final String colorClass;
    }
}
