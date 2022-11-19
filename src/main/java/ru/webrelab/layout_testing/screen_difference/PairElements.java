package ru.webrelab.layout_testing.screen_difference;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import ru.webrelab.layout_testing.LayoutElement;
import ru.webrelab.layout_testing.LayoutTestingException;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PairElements {
    private final LayoutElement actual;
    private final LayoutElement expected;
    private final boolean equalsSignature;
    private final boolean isEqualId;
    private final int violation;
    private final boolean equalsData;
    private boolean mapped = false;
    private boolean hasDifference = false;

    public PairElements(final LayoutElement actual, final LayoutElement expected) {
        this.actual = actual;
        this.expected = expected;
        equalsSignature = actual.compareSignature(expected);
        isEqualId = actual.getId().equals(expected.getId());
        violation =  equalsSignature ? actual.getDimensionViolation(expected) : -1;
        try {
            equalsData = equalsSignature
                    && actual.getTagName().equals(expected.getTagName())
                    && actual.getData().compareWith(expected.getData()).isEmpty();
        } catch (final NullPointerException e) {
            final Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println("Actual object:\n" + gson.toJson(actual));
            System.out.println("Expected object:\n" + gson.toJson(expected));
            throw new LayoutTestingException(e);
        }
    }

    public boolean isEqualsByViolation(final int violation) {
        mapped = equalsData && this.violation <= violation;
        return mapped;
    }

    public List<DifferentElements> getDifference() {
        final List<DifferentElements> diffs = new ArrayList<>();
        if (equalsSignature) {
            diffs.addAll(actual.getPosition().compareWith(expected.getPosition()));
            diffs.addAll(actual.getSize().compareWith(expected.getSize()));
            diffs.addAll(actual.getData().compareWith(expected.getData()));
        }
        return diffs;
    }

}
