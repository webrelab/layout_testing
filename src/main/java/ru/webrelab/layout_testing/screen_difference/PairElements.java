package ru.webrelab.layout_testing.screen_difference;

import lombok.Getter;
import lombok.Setter;
import ru.webrelab.layout_testing.LayoutElement;
import ru.webrelab.layout_testing.Tree;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PairElements {
    private final Tree<LayoutElement> actual;
    private final Tree<LayoutElement> expected;
    private final boolean equalsSignature;
    private final int violation;
    private final boolean equalsData;
    private boolean mapped = false;
    private boolean hasDifference = false;

    public PairElements(final Tree<LayoutElement> actual, final Tree<LayoutElement> expected) {
        this.actual = actual;
        this.expected = expected;
        equalsSignature = actual.getValue().compareSignature(expected.getValue());
        violation =  equalsSignature ? actual.getValue().getDimensionViolation(expected.getValue()) : -1;
        equalsData = equalsSignature && actual.getValue().getData().equals(expected.getValue().getData());
    }

    public boolean isEqualsByViolation(final int violation) {
        mapped = equalsData && this.violation <= violation;
        return mapped;
    }

    public List<DifferentElements> getDifference() {
        final List<DifferentElements> diffs = new ArrayList<>();
        if (equalsSignature) {
            diffs.addAll(actual.getValue().getPosition().compareWith(expected.getValue().getPosition()));
            diffs.addAll(actual.getValue().getSize().compareWith(expected.getValue().getSize()));
            diffs.addAll(actual.getValue().getData().compareWith(expected.getValue().getData()));
        }
        return diffs;
    }

}
