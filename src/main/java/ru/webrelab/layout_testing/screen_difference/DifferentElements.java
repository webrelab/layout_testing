package ru.webrelab.layout_testing.screen_difference;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public class DifferentElements {
    private static Function<DifferentElements, String> formatter = a ->
            String.format(
                    "Parameter '%s' has value '%s' but expected '%s'",
                    a.name,
                    a.actual,
                    a.expected
            );
    private final String name;
    private final Object actual;
    private final Object expected;

    @Override
    public String toString() {
        return formatter.apply(this);
    }

    public static void setCustomFormatter(final Function<DifferentElements, String> formatter) {
        DifferentElements.formatter = formatter;
    }
}
