package ru.webrelab.layout_testing.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.webrelab.layout_testing.ifaces.IScreenSize;

/**
 * Список доступных разрешений для выполнения тестов на вёрстку
 */
@RequiredArgsConstructor
@Getter
public enum ScreenSize implements IScreenSize {
    FULL_HD("1920x1080", 1902, 957),
    DESKTOP("1680x900", 1672, 777),
    DEFAULT(DESKTOP.description, DESKTOP.width, DESKTOP.height),
    TABLET_LANDSCAPE("1024x768", 1024, 645),
    TABLET_PORTRAIT("768x1024", 768, 901);

    private final String description;
    private final int width;
    private final int height;
}
