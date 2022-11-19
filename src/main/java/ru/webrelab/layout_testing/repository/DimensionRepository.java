package ru.webrelab.layout_testing.repository;

import ru.webrelab.layout_testing.LayoutConfiguration;
import ru.webrelab.layout_testing.ifaces.IRepository;
import ru.webrelab.layout_testing.screen_difference.DifferentElements;

import java.util.List;
import java.util.Map;

public abstract class DimensionRepository extends Repository {

    public int getViolationWith(final DimensionRepository expected) {
        int maxViolation = 0;
        final Map<String, Object> expectedFields = expected.getFieldMap();
        for (final Map.Entry<String, Object> actual : getFieldMap().entrySet()) {
            int violation = getDifference((Integer) actual.getValue(), (Integer) expectedFields.get(actual.getKey()));
            maxViolation = Math.max(maxViolation, violation);
        }
        return maxViolation;
    }

    @Override
    public List<DifferentElements> compareWith(IRepository expected) {
        return compareWith(expected, (act, exp) -> getDifference((int) act, (int) exp) > LayoutConfiguration.INSTANCE.getTolerance());
    }

    private int getDifference(final int actual, final int expected) {
        if (actual == expected) return 0;
        int pixelDiff = Math.abs(actual - expected);
        int percentDiff = expected == 0 ? 0 : (int) (Math.abs(1 - actual / (double) expected) * 100);
        return Math.min(pixelDiff, percentDiff);
    }

    @Override
    public boolean check() {
        return true;
    }
}
