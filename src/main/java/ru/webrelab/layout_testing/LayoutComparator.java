package ru.webrelab.layout_testing;

import lombok.RequiredArgsConstructor;
import ru.webrelab.layout_testing.screen_difference.*;

import java.util.List;

@RequiredArgsConstructor
public class LayoutComparator {
    private final Tree<LayoutElement> actualTree;
    private final Tree<LayoutElement> expectedTree;

    public List<DifferenceReport> compare() {
        final PairCollection pairCollection = new PairCollection();
        for (final Tree<LayoutElement> actual : actualTree.getChildren()) {
            for (final Tree<LayoutElement> expected : expectedTree.getChildren()) {
                pairCollection.add(new PairElements(actual, expected));
            }
        }
        final PairSifter sifter = new PairSifter(pairCollection);
        final List<DifferenceReport> reports = sifter.sift();
        sifter.getEquivalentPairs().forEach(p -> {
            final LayoutComparator comparator = new LayoutComparator(p.getActual(), p.getExpected());
            reports.addAll(comparator.compare());
        });
        return reports;
    }
}
