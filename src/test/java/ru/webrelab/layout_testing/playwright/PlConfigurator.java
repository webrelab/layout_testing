package ru.webrelab.layout_testing.playwright;

import ru.webrelab.layout_testing.LayoutConfiguration;
import ru.webrelab.layout_testing.framework_based_behavior.PlaywrightFrameworkBehavior;

public class PlConfigurator {
    public void configure() {
        LayoutConfiguration.INSTANCE.setMethodsInjection(new PlMethodsInjectionImpl());
        LayoutConfiguration.INSTANCE.setFrameworkBasedBehavior(new PlaywrightFrameworkBehavior());
        LayoutConfiguration.INSTANCE.setTolerance(1);
    }
}
