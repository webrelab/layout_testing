package ru.webrelab.layout_testing.repository;

import ru.webrelab.layout_testing.LayoutConfiguration;
import ru.webrelab.layout_testing.ifaces.IRepository;
import ru.webrelab.layout_testing.screen_difference.DifferentElements;
import ru.webrelab.layout_testing.utils.ElementAttributesUtil;

import java.util.List;

public abstract class AttributeRepository extends Repository {
    public AttributeRepository(final Object object) {
    }

    public AttributeRepository(final Object... objects) {
    }

    @Override
    public List<DifferentElements> compareWith(IRepository expected) {
        return compareWith(expected, (act, exp) -> !act.equals(exp));
    }

    @Override
    public PositionRepository getPosition(final PositionRepository container, final Object webElement) {
        return LayoutConfiguration.INSTANCE.getMethodsInjection().getPosition(container, webElement);
    }

    @Override
    public SizeRepository getSize(final Object webElement) {
        return LayoutConfiguration.INSTANCE.getFrameworkBasedBehavior().getSize(webElement);
    }

    @Override
    public String getTransform(final Object webElement) {
        return (String) ElementAttributesUtil.getStyles(webElement).get("transform");
    }
}
