package ru.webrelab.layout_testing.framework_based_behavior;

import ru.webrelab.layout_testing.LayoutConfiguration;
import ru.webrelab.layout_testing.ifaces.IFrameworkBasedBehavior;
import ru.webrelab.layout_testing.ifaces.IMethodsInjection;

public class SeleniumFrameworkBehavior implements IFrameworkBasedBehavior {
    private final IMethodsInjection methods = LayoutConfiguration.INSTANCE.getMethodsInjection();

    @Override
    public Object jsExecutor(final String js, final Object webElement) {
        return methods.executeJs("return (function (e) {return handler(e);}(arguments[0]));\n" + js, webElement);
    }
}
