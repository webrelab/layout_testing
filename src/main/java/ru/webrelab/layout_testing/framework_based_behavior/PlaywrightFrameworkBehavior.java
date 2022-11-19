package ru.webrelab.layout_testing.framework_based_behavior;

import ru.webrelab.layout_testing.LayoutConfiguration;
import ru.webrelab.layout_testing.ifaces.IFrameworkBasedBehavior;
import ru.webrelab.layout_testing.ifaces.IMethodsInjection;
import ru.webrelab.layout_testing.ifaces.ISnippetEnum;
import ru.webrelab.layout_testing.snippets.SnippetsRepository;

public class PlaywrightFrameworkBehavior implements IFrameworkBasedBehavior {
    private final IMethodsInjection methods = LayoutConfiguration.INSTANCE.getMethodsInjection();
    @Override
    public Object jsExecutor(String js, Object webElement) {
        return methods.executeJs("arg => handler(arg);\n" + js, webElement);
    }
}
