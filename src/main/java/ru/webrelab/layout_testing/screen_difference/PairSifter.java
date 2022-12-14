package ru.webrelab.layout_testing.screen_difference;

import lombok.Getter;
import ru.webrelab.layout_testing.LayoutConfiguration;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * The class performs a sifting of pairs of elements (obtained from the screen and from the snapshot),
 * iteratively displacing first clearly different and identical elements, then identical in attributes
 * and with allowable deviations in position and size. Of the remaining ones, matching is performed
 * by position or attribute and errors are calculated.
 */
public class PairSifter {
    private final PairCollection pairElements;
    private final List<String> sievedActual = new ArrayList<>();
    private final List<String> sievedExpected = new ArrayList<>();

    @Getter
    private final PairCollection equivalentPairs = new PairCollection();

    private final List<DifferenceReport> reports = new ArrayList<>();

    public PairSifter(PairCollection pairElements) {
        this.pairElements = pairElements;
    }

    public List<DifferenceReport> sift() {
        cleanEquals();
        cleanEqualsWithViolation();
        searchForDataDifference();
        searchForSizeDifference();
        searchForLostAndExtraElements();
        return reports;
    }

    private void cleanEquals() {
        sieveStream(
                equivalentPairs::add,
                pair -> pair.isEqualsByViolation(0)
        );
        removePairsContainsSievedElements();
    }

    private void cleanEqualsWithViolation() {
        sieveStream(
                equivalentPairs::add,
                p -> p.isEqualsData()
                        && p.getViolation() < LayoutConfiguration.INSTANCE.getTolerance()
        );
        removePairsContainsSievedElements();
    }

    private void searchForDataDifference() {
        sieveStream(
                pair -> reports.add(new DifferenceReport(pair)),
                p -> p.isEqualsSignature() && !p.isEqualsData() && p.getViolation() < LayoutConfiguration.INSTANCE.getTolerance()
        );
        removePairsContainsSievedElements();
    }

    private void searchForSizeDifference() {
        sieveStream(
                pair -> reports.add(new DifferenceReport(pair)),
                PairElements::isEqualsData
        );
        removePairsContainsSievedElements();
    }

    private void searchForLostAndExtraElements() {
        pairElements.forEach(pair -> {
            if (!sievedActual.contains(pair.getActual().getId()) && sievedExpected.contains(pair.getExpected().getId())) {
                sievedActual.add(pair.getActual().getId());
                reports.add(new DifferenceReport(pair.getActual(), null));
            } else if (sievedActual.contains(pair.getActual().getId()) && !sievedExpected.contains(pair.getExpected().getId())) {
                sievedExpected.add(pair.getExpected().getId());
                reports.add(new DifferenceReport(null, pair.getExpected()));
            }
        });
        removePairsContainsSievedElements();
    }

    private void sieveStream(final Consumer<PairElements> reportCollect, final Predicate<? super PairElements> predicate) {
        final Set<String> localActual = new HashSet<>();
        final Set<String> localExpected = new HashSet<>();
        pairElements.stream()
                .filter(predicate)
                .sorted(Comparator.comparingInt(PairElements::getViolation).thenComparing(p -> p.isEqualId() ? -1 : 1))
                .forEachOrdered(pair -> {
                    if (!localActual.contains(pair.getActual().getId()) && !localExpected.contains(pair.getExpected().getId())) {
                        localActual.add(pair.getActual().getId());
                        localExpected.add(pair.getExpected().getId());
                        sievedActual.add(pair.getActual().getId());
                        sievedExpected.add(pair.getExpected().getId());
                        if (reportCollect != null) {
                            reportCollect.accept(pair);
                        }
                    }
                });
    }

    private void removePairsContainsSievedElements() {
        pairElements.removeIf(p -> sievedActual.contains(p.getActual().getId()) &&
                sievedExpected.contains(p.getExpected().getId()));
    }
}
