package ru.webrelab.layout_testing.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.webrelab.layout_testing.utils.ElementAttributesUtil;

import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Getter
public class DecorRepository extends AttributeRepository {
    private final String border;
    private final String borderRadius;
    private final String boxShadow;
    private final String background;

    public DecorRepository(Object webElement) {
        final Map<String, Object> styles = ElementAttributesUtil.getStyles(webElement);
        border = Objects.requireNonNull((String) styles.get("border"));
        borderRadius = Objects.requireNonNull((String) styles.get("borderRadius"));
        boxShadow = Objects.requireNonNull((String) styles.get("boxShadow"));
        background = Objects.requireNonNull((String) styles.get("background"));
    }

    @Override
    public boolean check() {
        return true;
    }
}
