package ru.webrelab.layout_testing;

import lombok.SneakyThrows;
import ru.webrelab.layout_testing.ifaces.IMeasuringType;
import ru.webrelab.layout_testing.ifaces.IMethodsInjection;
import ru.webrelab.layout_testing.ifaces.IRepository;
import ru.webrelab.layout_testing.repository.PositionRepository;
import ru.webrelab.layout_testing.repository.SizeRepository;

import java.util.Objects;
import java.util.stream.Collectors;

public class PageScanner {
    private final String name;
    private final PositionRepository container;
    private final Object element;
    private final IMeasuringType type;
    private final IMethodsInjection methods = LayoutConfiguration.INSTANCE.getMethods();

    public PageScanner(String name, PositionRepository container, Object element, IMeasuringType type) {
        this.name = name;
        this.container = container;
        this.element = element;
        this.type = type;
    }

    public LayoutCollection scan() {
        final String typeName = ((Enum<?>) type).name();

        if ("POSITION".equals(typeName)) {
            final LayoutCollection elements = new LayoutCollection();
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
        }
        return scan(type);
    }

    private LayoutCollection scan(final IMeasuringType type) {
        return LayoutConfiguration.INSTANCE.getMethods().findElementsByXpath(element, type.getXpath())
                .stream()
                .map(o -> createLayoutElement(o, type))
                .filter(Objects::nonNull)
                .filter(e -> e.getData().check())
                .collect(Collectors.toMap(LayoutElement::getId, e -> e, (a, b) -> b, LayoutCollection::new));
    }

    @SneakyThrows
    private LayoutElement createLayoutElement(final Object subElement, final IMeasuringType type) {

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