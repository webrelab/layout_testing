package ru.webrelab.layout_testing;

import lombok.SneakyThrows;
import ru.webrelab.layout_testing.ifaces.IMeasuringType;
import ru.webrelab.layout_testing.ifaces.IMethodsInjection;
import ru.webrelab.layout_testing.ifaces.IRepository;
import ru.webrelab.layout_testing.repository.PositionRepository;
import ru.webrelab.layout_testing.repository.SizeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PageScanner {
    private final String name;
    private final PositionRepository container;
    private final Object element;
    private final IMeasuringType type;
    private final IMethodsInjection methods = LayoutConfiguration.INSTANCE.getMethods();

    public PageScanner(String name, Object container, Object element, IMeasuringType type) {
        this.name = name;
        this.container = LayoutConfiguration.INSTANCE.getMethods().getPosition(new PositionRepository(0, 0), container);
        this.element = element;
        this.type = type;
    }

    public List<LayoutElement> scan() {
        final String typeName = ((Enum<?>) type).name();

        switch (typeName) {
            case "COMPLEX":
            case "ALL":
                return Stream.of(LayoutConfiguration.INSTANCE.getDefaultMeasuringTypeEnum().getEnumConstants())
                        .filter(IMeasuringType::isComplex)
                        .map(this::scan)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
            case "POSITION":
                final List<LayoutElement> elements = new ArrayList<>();
                final SizeRepository size = methods.getSize(element);
                if (size.isEmpty()) return elements;
                final PositionRepository position = methods.getPosition(container, element);
                final String tagName = methods.getTagName(element);
                final LayoutElement layoutElement =
                        new LayoutElement(
                                name,
                                tagName,
                                type,
                                position,
                                size,
                                null,
                                element
                        );
                elements.add(layoutElement);
                return elements;
            default:
                return scan(type);
        }
    }

    private List<LayoutElement> scan(final IMeasuringType type) {
        return LayoutConfiguration.INSTANCE.getMethods().findElementsByXpath(element, type.getXpath())
                .stream()
                .map(o -> createLayoutElement(o, type))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private LayoutElement createLayoutElement(final Object subElement, final IMeasuringType type) {
        final IMethodsInjection methods = LayoutConfiguration.INSTANCE.getMethods();
        final SizeRepository size = methods.getSize(subElement);
        if (size.isEmpty()) return null;
        final PositionRepository position = methods.getPosition(container, subElement);
        final IRepository measuredData = type.getRepositoryClass().getConstructor(Object.class).newInstance(subElement);
        final String tagName = methods.getTagName(subElement);
        return new LayoutElement(
                name,
                tagName,
                type,
                position,
                size,
                measuredData,
                subElement
        );
    }
}
