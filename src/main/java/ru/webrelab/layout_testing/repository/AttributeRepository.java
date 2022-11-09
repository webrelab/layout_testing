package ru.webrelab.layout_testing.repository;

import ru.webrelab.layout_testing.ifaces.IRepository;
import ru.webrelab.layout_testing.screen_difference.DifferentElements;

import java.util.List;

public abstract class AttributeRepository extends Repository {
    public AttributeRepository(final Object object) {}
    public AttributeRepository(final Object... objects) {}

    @Override
    public List<DifferentElements> compareWith(IRepository expected) {
        return compareWith(expected, (act, exp) -> !act.equals(exp));
    }
}
