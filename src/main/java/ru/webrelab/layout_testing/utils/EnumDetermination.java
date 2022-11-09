package ru.webrelab.layout_testing.utils;

import ru.webrelab.layout_testing.LayoutConfiguration;
import ru.webrelab.layout_testing.LayoutTestingException;
import ru.webrelab.layout_testing.ifaces.IMeasuringType;
import ru.webrelab.layout_testing.ifaces.IScreenSize;

import java.util.stream.Stream;

public class EnumDetermination {
    public static IMeasuringType determineMeasureEnum(final String enumName) {
        return Stream.of(LayoutConfiguration.INSTANCE.getDefaultMeasuringTypeEnum().getEnumConstants())
                .filter(c -> enumName.equalsIgnoreCase(((Enum<?>) c).name()))
                .findFirst()
                .orElseThrow(() -> new LayoutTestingException(
                        String.format(
                                "Enum '%s' don't contain constant '%s'",
                                LayoutConfiguration.INSTANCE.getDefaultMeasuringTypeEnum().getName(),
                                enumName
                        )
                ));
    }

    public static IScreenSize determineScreenSizeEnum(final String enumName) {
        return Stream.of(LayoutConfiguration.INSTANCE.getDefaultScreenSizeEnum().getEnumConstants())
                .filter(c -> enumName.equalsIgnoreCase(((Enum<?>) c).name()))
                .findFirst()
                .orElseThrow(() -> new LayoutTestingException(
                        String.format(
                                "Enum '%s' don't contain constant '%s'",
                                LayoutConfiguration.INSTANCE.getDefaultScreenSizeEnum().getName(),
                                enumName
                        )
                ));
    }
}
