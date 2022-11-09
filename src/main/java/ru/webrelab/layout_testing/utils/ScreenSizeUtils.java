package ru.webrelab.layout_testing.utils;

import ru.webrelab.layout_testing.LayoutConfiguration;
import ru.webrelab.layout_testing.LayoutTestingException;
import ru.webrelab.layout_testing.ifaces.IMethodsInjection;
import ru.webrelab.layout_testing.ifaces.IScreenSize;
import ru.webrelab.layout_testing.repository.SizeRepository;

import java.util.stream.Stream;

public class ScreenSizeUtils {

    public static void setWindowSize(final String screenSizeEnumName) {
        setWindowSize(EnumDetermination.determineScreenSizeEnum(screenSizeEnumName));
    }

    public static void setWindowSize(final IScreenSize screenSize) {
        final IMethodsInjection methods = LayoutConfiguration.INSTANCE.getMethods();
        SizeRepository currentSize = methods.getWindowBodySize();
        int counter = 0;
        while (
                Math.abs(currentSize.getHeight() - screenSize.getHeight()) > 2 &&
                        Math.abs(currentSize.getWidth() - screenSize.getWidth()) > 2
        ) {
            final SizeRepository windowSize = methods.getWindowSize();
            final SizeRepository newWindowSize = new SizeRepository(
                    windowSize.getHeight() - currentSize.getHeight() + screenSize.getHeight(),
                    windowSize.getWidth() - currentSize.getWidth() + screenSize.getWidth()
            );
            methods.setWindowSize(newWindowSize);
            currentSize = methods.getWindowBodySize();
            if (++counter > 5) {
                throw new LayoutTestingException(String.format("Screen resolution '%s' can't be set", screenSize));
            }
        }
    }

    public static IScreenSize determineScreenSize() {
        final SizeRepository currentSize = LayoutConfiguration.INSTANCE.getMethods().getWindowBodySize();
        return Stream.of(LayoutConfiguration.INSTANCE.getDefaultScreenSizeEnum().getEnumConstants())
                .filter(d -> Math.abs(d.getHeight() - currentSize.getHeight()) < 3 &&
                        Math.abs(d.getWidth() - currentSize.getWidth()) < 3)
                .findFirst()
                .orElseThrow(
                        () -> new LayoutTestingException(
                                String.format(
                                        "The current screen size (w: %d, h: %d) doesn't match the standard sizes",
                                        currentSize.getWidth(),
                                        currentSize.getHeight()
                                )
                        )
                );
    }
}
