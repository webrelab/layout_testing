package ru.webrelab.layout_testing.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.webrelab.layout_testing.LayoutConfiguration;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public class DecorRepository extends AttributeRepository {
    private final String border;
    private final String borderRadius;
    private final String boxShadow;
    private final String background;

    @SuppressWarnings("unchecked")
    public DecorRepository(Object object) {
        final Map<String, Object> styles =
                (Map<String, Object>) LayoutConfiguration.INSTANCE.getMethods().executeJs("return window.getComputedStyle(arguments[0])", object);
        border = (String) styles.get("border");
        borderRadius = (String) styles.get("borderRadius");
        boxShadow = (String) styles.get("boxShadow");
        background = (String) styles.get("background");
    }
}
