package ru.webrelab.layout_testing.screen_difference;

import lombok.Getter;
import ru.webrelab.layout_testing.LayoutElement;
import ru.webrelab.layout_testing.LayoutTestingException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class DifferenceReport {
    private static Function<DifferenceReport, String> formatter = report ->
            String.format("For the element '%s' with ID '%s' found one or more errors", report.name, report.id);
    private final List<DifferentElements> differentElements = new ArrayList<>();
    private final List<ExistenceState> existenceStates = new ArrayList<>();
    private final LayoutElement actual;
    private final LayoutElement expected;
    private final String name;
    private final String id;
    private final boolean elementNotFound;
    private final boolean extraElement;

    public DifferenceReport(final PairElements pair) {
        this(pair.getActual(), pair.getExpected());
        if (!expected.isIgnore() && elementNotFound == extraElement) addDiff(pair.getDifference());
    }

    public DifferenceReport(final LayoutElement actual, final LayoutElement expected) {
        this.actual = actual;
        this.expected = expected;
        elementNotFound = actual == null;
        extraElement = expected == null;
        if (elementNotFound && extraElement) {
            throw new LayoutTestingException("The actual and expected elements must not be null");
        }
        if (elementNotFound != extraElement) {
            existenceStates.add(new ExistenceState(extraElement));
        }
        name = extraElement ? actual.getName() : expected.getName();
        id = extraElement ? actual.getId() : expected.getId();
    }

    public static void setCustomFormatter(final Function<DifferenceReport, String> formatter) {
        DifferenceReport.formatter = formatter;
    }

    @Override
    public String toString() {
        final List<String> messages = Stream.concat(
                differentElements.stream()
                        .map(DifferentElements::toString),
                existenceStates.stream()
                        .map(ExistenceState::toString)
        ).collect(Collectors.toList());
        if (!messages.isEmpty()) {
            return formatter.apply(this) + "\n\n" + String.join("\n\n", messages);
        }
        return "";
    }

    private void addDiff(final Collection<DifferentElements> diffs) {
        differentElements.addAll(diffs);
    }
}
