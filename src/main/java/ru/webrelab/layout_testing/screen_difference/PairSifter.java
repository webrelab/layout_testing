package ru.webrelab.layout_testing.screen_difference;

import lombok.Getter;
import ru.webrelab.layout_testing.LayoutConfiguration;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Класс выполняет просеивание пар элементов (полученные с экрана и из снэпшота)
 * итеративно вытесняя сначала явно различные и идентичные элементы, затем идентичные
 * по атрибутам и имеющие допустимые отклонения по позиции и размеру. Из оставшихся
 * выполняется сопоставление по позициям или атрибутам и вычисляются ошибки.
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
        searchForFullDifference();
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
                p -> p.isEqualsSignature() && p.getViolation() < LayoutConfiguration.INSTANCE.getTolerance()
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

    private void searchForFullDifference() {
        sieveStream(pair -> reports.add(new DifferenceReport(pair)), p -> true);
        removePairsContainsSievedElements();
    }

    private void searchForLostAndExtraElements() {
        sieveStream(
                pair -> {
                    if (!sievedActual.contains(pair.getActual().getId())) {
                        reports.add(new DifferenceReport(pair.getActual(), null));
                    } else if (!sievedExpected.contains(pair.getExpected().getId())) {
                        reports.add(new DifferenceReport(null, pair.getExpected()));
                    }
                },
                p -> true
        );
    }

    private void sieveStream(final Consumer<PairElements> reportCollect, final Predicate<? super PairElements> predicate) {
        final Map<String, PairElements> sieved = new HashMap<>();
        pairElements.stream()
                .filter(predicate)
                .sorted(Comparator.comparingInt(PairElements::getViolation))
                .forEachOrdered(pair -> {
                    if (!sieved.containsKey(pair.getActual().getId())) {
                        sieved.put(pair.getActual().getId(), pair);
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
