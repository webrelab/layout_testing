package ru.webrelab.layout_testing;

import lombok.Getter;
import lombok.Setter;
import ru.webrelab.layout_testing.enums.MeasuringType;
import ru.webrelab.layout_testing.enums.ScreenSize;
import ru.webrelab.layout_testing.ifaces.IMeasuringType;
import ru.webrelab.layout_testing.ifaces.IMethodsInjection;
import ru.webrelab.layout_testing.ifaces.IScreenSize;

import java.util.Objects;

@Getter
@Setter
public class LayoutConfiguration {
    public static final LayoutConfiguration INSTANCE = new LayoutConfiguration();
    private int tolerance = 5;
    private Class<? extends IMeasuringType> defaultMeasuringTypeEnum = MeasuringType.class;
    private Class<? extends IScreenSize> defaultScreenSizeEnum = ScreenSize.class;
    private String actualElementColor = "red";
    private String expectedElementColor = "green";
    private IMethodsInjection methodsInjection;

    private LayoutConfiguration() {}

    public IMethodsInjection getMethods() {
        if (Objects.isNull(methodsInjection)) {
            throw new LayoutTestingException("You must set 'methodsInjection' in LayoutConfiguration class");
        }
        return methodsInjection;
    }
}
