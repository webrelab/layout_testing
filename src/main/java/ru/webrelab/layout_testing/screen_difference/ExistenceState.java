package ru.webrelab.layout_testing.screen_difference;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public class ExistenceState {
    private static Function<ExistenceState, String> looseElementFormatter = state ->
            "Expected element not found";
    private static Function<ExistenceState, String> extraElementFormatter = state ->
            "No match found for element";
    private final boolean isExtra;


    @Override
    public String toString() {
        return isExtra ? extraElementFormatter.apply(this) : looseElementFormatter.apply(this);
    }

    public static void setLooseElementFormatter(final Function<ExistenceState, String> looseElementFormatter) {
        ExistenceState.looseElementFormatter = looseElementFormatter;
    }
    public static void setExtraElementFormatter(final Function<ExistenceState, String> extraElementFormatter) {
        ExistenceState.extraElementFormatter = extraElementFormatter;
    }
}
