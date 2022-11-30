package ru.webrelab.layout_testing.repository;

import ru.webrelab.layout_testing.LayoutConfiguration;
import ru.webrelab.layout_testing.ifaces.IRepository;
import ru.webrelab.layout_testing.screen_difference.DifferentElements;
import ru.webrelab.layout_testing.utils.ElementAttributesUtil;

import java.util.List;
import java.util.regex.Pattern;

public abstract class AttributeRepository extends Repository {

    @Override
    public List<DifferentElements> compareWith(IRepository expected) {
        return compareWith(expected, (act, exp) -> !equalsWithMask(act, exp));
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

    protected boolean equalsWithMask(final Object act, final Object exp) {
        if (!(act instanceof String) || !(exp instanceof String)) return act.equals(exp);
        final String actual = (String) act;
        final String expected = (String) exp;
        if (!expected.contains("*")) return actual.equals(expected);
        if (expected.equals("*")) return true;
        if (Pattern.compile("^\\*+$").matcher(expected).find()) return actual.length() > 0;
        final boolean mustStartsWith = !expected.startsWith("*");
        final boolean mustEndsWith = !expected.endsWith("*");
        final String[] expectedParts = expected.split("\\*");
        int currentPosition = 0;
        final int LAST = expectedParts.length - 1;
        for (int i = 0; i < expectedParts.length; i++) {
            int foundIndex = actual.indexOf(expectedParts[i], currentPosition);
            if (foundIndex < 0) return false;
            currentPosition = foundIndex + expectedParts[i].length();
            if (i == 0 && mustStartsWith && foundIndex != 0) return false;
            if (i == LAST && mustEndsWith && foundIndex != actual.length() - expectedParts[LAST].length()) return false;
        }
        return true;
    }
}
