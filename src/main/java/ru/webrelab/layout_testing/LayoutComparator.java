package ru.webrelab.layout_testing;

import lombok.RequiredArgsConstructor;
import ru.webrelab.layout_testing.screen_difference.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LayoutComparator {
    private final LayoutCollection actualElements;
    private final LayoutCollection expectedElements;

    public List<DifferenceReport> compareRoot() {
        final PairCollection pairCollection = new PairCollection();
        for (final LayoutElement actual : getRootElements(actualElements)) {
            for (final LayoutElement expected : getRootElements(expectedElements)) {
                pairCollection.add(new PairElements(actual, expected));
            }
        }
        return comparePairs(pairCollection);
    }

    public List<DifferenceReport> comparePairs(final PairCollection pairCollection) {
        final PairSifter sifter = new PairSifter(pairCollection);
        final List<DifferenceReport> reports = sifter.sift();
        for (final PairElements pair : sifter.getEquivalentPairs()) {
            final PairCollection internalPairCollection = new PairCollection();
            if (pair.getActual().getChildren().isEmpty() != pair.getExpected().getChildren().isEmpty()) {
                pair.getActual().getChildren().forEach(child -> reports.add(
                        new DifferenceReport(actualElements.get(child), null)
                ));
                pair.getExpected().getChildren().forEach(child -> reports.add(
                        new DifferenceReport(null, expectedElements.get(child))
                ));
                continue;
            }
            for (final String actual : pair.getActual().getChildren()) {
                for (final String expected : pair.getExpected().getChildren()) {
                    internalPairCollection.add(new PairElements(actualElements.get(actual), expectedElements.get(expected)));
                }
            }
            if (!internalPairCollection.isEmpty()) {
                reports.addAll(new LayoutComparator(actualElements, expectedElements).comparePairs(internalPairCollection));
            }
        }
        return reports;
    }

    private List<LayoutElement> getRootElements(final LayoutCollection elements) {
        return elements.values().stream()
                .filter(e -> e.getParent().isEmpty())
                .collect(Collectors.toList());

    }
}
