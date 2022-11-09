package ru.webrelab.layout_testing.screen_difference;

import lombok.Getter;
import ru.webrelab.layout_testing.LayoutElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class DifferenceReport {
    private final List<DifferentElements> differentElements = new ArrayList<>();
    private final LayoutElement actual;
    private final LayoutElement expected;
    private final boolean elementNotFound;
    private final boolean extraElement;

    public DifferenceReport(final PairElements pair) {
        this(pair.getActual().getValue(), pair.getExpected().getValue());
        if (!expected.isIgnore()) addDiff(pair.getDifference());
    }

    public DifferenceReport(LayoutElement actual, LayoutElement expected) {
        this.actual = actual;
        this.expected = expected;
        elementNotFound = actual == null;
        extraElement = expected == null;
    }

    private void addDiff(final Collection<DifferentElements> diffs) {
        differentElements.addAll(diffs);
    }
}
