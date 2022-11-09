package ru.webrelab.layout_testing.screen_difference;

import lombok.Getter;
import ru.webrelab.layout_testing.LayoutConfiguration;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

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
        pairElements.removeIf(pair -> !pair.isEqualsSignature());
        cleanEquals();
        cleanEqualsWithViolation();
        searchForSizeDifference();
        searchForDataDifference();
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
                PairElements::isEqualsData,
                p -> p.getViolation() < LayoutConfiguration.INSTANCE.getTolerance()
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

    private void searchForDataDifference() {
        sieveStream(
                pair -> reports.add(new DifferenceReport(pair)),
                p -> p.getViolation() < LayoutConfiguration.INSTANCE.getTolerance()
        );
        removePairsContainsSievedElements();
    }

    private void searchForFullDifference() {
        sieveStream(pair -> reports.add(new DifferenceReport(pair)));
        removePairsContainsSievedElements();
    }

    private void searchForLostAndExtraElements() {
        sieveStream(
                pair -> {
                    if (!sievedActual.contains(pair.getActual().getValue().getUuid())) {
                        reports.add(new DifferenceReport(pair.getActual().getValue(), null));
                    } else if (!sievedExpected.contains(pair.getExpected().getValue().getUuid())) {
                        reports.add(new DifferenceReport(null, pair.getExpected().getValue()));
                    }
                }
        );
    }

    @SafeVarargs
    private void sieveStream(final Consumer<PairElements> reportCollect, final Predicate<? super PairElements>... predicates) {
        final Map<String, PairElements> sieved = new HashMap<>();
        Stream<PairElements> stream = pairElements.stream();
        for (final Predicate<? super PairElements> predicate : predicates) {
            stream = stream.filter(predicate);
        }
        stream.sorted(Comparator.comparingInt(PairElements::getViolation))
                .forEachOrdered(pair -> {
                    if (!sieved.containsKey(pair.getActual().getValue().getUuid())) {
                        sieved.put(pair.getActual().getValue().getUuid(), pair);
                        sievedActual.add(pair.getActual().getValue().getUuid());
                        sievedExpected.add(pair.getExpected().getValue().getUuid());
                        if (reportCollect != null) reportCollect.accept(pair);
                    }
                });
    }

    private void removePairsContainsSievedElements() {
        pairElements.removeIf(p -> sievedActual.contains(p.getActual().getValue().getUuid()) &&
                sievedExpected.contains(p.getExpected().getValue().getUuid()));
    }
}
