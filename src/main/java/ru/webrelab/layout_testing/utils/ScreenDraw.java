package ru.webrelab.layout_testing.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.webrelab.layout_testing.LayoutConfiguration;
import ru.webrelab.layout_testing.LayoutElement;
import ru.webrelab.layout_testing.ifaces.IMethodsInjection;
import ru.webrelab.layout_testing.repository.PositionRepository;
import ru.webrelab.layout_testing.screen_difference.DifferenceReport;
import ru.webrelab.layout_testing.snippets.Snippet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ScreenDraw {
    private final PositionRepository container;

    public ScreenDraw(final PositionRepository container) {
        final IMethodsInjection methods = LayoutConfiguration.INSTANCE.getMethodsInjection();
        final PositionRepository body = methods.getPosition(null, methods.findElementsByXpath("//body").get(0));
        this.container = new PositionRepository(container.getTop() - body.getTop(), container.getLeft() - body.getLeft());
        createContainer();
    }

    public void draw(final List<DifferenceReport> reports) {
        reports.forEach(this::draw);
    }

    private void draw(final DifferenceReport report) {
        if (report.getActual() != null) draw(DataState.ACTUAL, report.getActual());
        if (report.getExpected() != null) draw(DataState.EXPECTED, report.getExpected());
    }

    public void draw(final DataState dataState, final LayoutElement layoutElement) {
        final Map<String, Object> block = new HashMap<>();
        block.put("top", layoutElement.getPosition().getTop() + container.getTop());
        block.put("left", layoutElement.getPosition().getLeft() + container.getLeft());
        block.put("height", layoutElement.getSize().getHeight());
        block.put("width", layoutElement.getSize().getWidth());
        block.put("className", dataState == null ? layoutElement.getType().toString(): dataState.name());
        block.put("transform", layoutElement.getTransform());
        final String elementId;
        if (dataState == null) elementId = layoutElement.getType().toString() + "-" + layoutElement.getId();
        else elementId = dataState + "-" + layoutElement.getType().toString() + "-" + layoutElement.getId();
        block.put("id", elementId);

        LayoutConfiguration.INSTANCE
                .getFrameworkBasedBehavior().jsExecutor(Snippet.GREED_DRAW, block);
    }

    private void createContainer() {
        final List<Map<String, Object>> params = new ArrayList<>();
        Stream.of(LayoutConfiguration.INSTANCE.getDefaultMeasuringTypeEnum().getEnumConstants())
                        .forEach(type -> params.add(Map.of(
                                "type", type.toString(),
                                "deg", type.getDeg(),
                                "color", type.getColor()
                        )));
        params.add(Map.of(
                "type", "actual",
                "deg", 45,
                "color", LayoutConfiguration.INSTANCE.getActualElementColor()
        ));
        params.add(Map.of(
                "type", "expected",
                "deg", -45,
                "color", LayoutConfiguration.INSTANCE.getExpectedElementColor()
        ));
        LayoutConfiguration.INSTANCE.getFrameworkBasedBehavior().jsExecutor(
                Snippet.CREATE_CONTAINER,
                params
        );
    }

    @RequiredArgsConstructor
    @Getter
    public enum DataState {
        ACTUAL,
        EXPECTED
    }
}
