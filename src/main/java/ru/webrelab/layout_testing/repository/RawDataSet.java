package ru.webrelab.layout_testing.repository;

import lombok.Getter;
import ru.webrelab.layout_testing.LayoutConfiguration;
import ru.webrelab.layout_testing.enums.MeasuringType;
import ru.webrelab.layout_testing.ifaces.IMeasuringType;
import ru.webrelab.layout_testing.utils.EnumDetermination;

import java.util.stream.Stream;

@Getter
public class RawDataSet {
    private final String elementName;
    private final Object element;
    private final IMeasuringType[] measureTypes;

    public RawDataSet(final String elementName, final Object element, final String measureTypes) {
        this.elementName = elementName;
        this.element = element;
        this.measureTypes = Stream.of(measureTypes.split("[\\s,;]"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(EnumDetermination::determineMeasureEnum)
                .flatMap(this::processMeasureType)
                .toArray(IMeasuringType[]::new);
    }

    private Stream<? extends IMeasuringType> processMeasureType(final IMeasuringType type) {
        if (
                MeasuringType.COMPLEX.name().equals(((Enum<?>) type).name()) ||
                        MeasuringType.ALL.name().equals(((Enum<?>) type).name())
        ) {
            return Stream.of(LayoutConfiguration.INSTANCE.getDefaultMeasuringTypeEnum().getEnumConstants())
                    .filter(IMeasuringType::isComplex);
        }
        return Stream.of(type);
    }
}
