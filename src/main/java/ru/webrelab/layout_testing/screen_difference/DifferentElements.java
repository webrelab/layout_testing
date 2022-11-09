package ru.webrelab.layout_testing.screen_difference;

import java.util.function.Function;

public class DifferentElements {
    private static Function<DifferentElements, String> formatter = a -> String.format("Parameter '%s' has value '%s' but expected '%s'", a.name, a.actual, a.expected);
    public final String name;
    public final Object actual;
    public final Object expected;

    public DifferentElements(String name, Object actual, Object expected) {
        this.name = name;
        this.actual = actual;
        this.expected = expected;
    }

    @Override
    public String toString() {
        return formatter.apply(this);
    }

    public static void setCustomFormatter(final Function<DifferentElements, String> formatter) {
        DifferentElements.formatter = formatter;
    }
}
