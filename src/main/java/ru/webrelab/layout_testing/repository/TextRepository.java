package ru.webrelab.layout_testing.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.webrelab.layout_testing.LayoutConfiguration;
import ru.webrelab.layout_testing.utils.ElementStylesUtil;

import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Getter
public class TextRepository extends AttributeRepository {
    private final String fontFamily;
    private final String fontSize;
    private final String fontWeight;
    private final String color;
    private final String fontStyle;
    private final String fontVariant;
    private final String textDecoration;
    private final String textShadow;
    private final String content;

    public TextRepository(final Object webElement) {
        final Map<String, Object> styles = ElementStylesUtil.getStyles(webElement);
        fontFamily = Objects.requireNonNull((String) styles.get("fontFamily"));
        fontSize = Objects.requireNonNull((String) styles.get("fontSize"));
        fontWeight = Objects.requireNonNull((String) styles.get("fontWeight"));
        color = Objects.requireNonNull((String) styles.get("color"));
        fontStyle = Objects.requireNonNull((String) styles.get("fontStyle"));
        fontVariant = Objects.requireNonNull((String) styles.get("fontVariant"));
        textDecoration = Objects.requireNonNull((String) styles.get("textDecoration"));
        textShadow = Objects.requireNonNull((String) styles.get("textShadow"));
        content = LayoutConfiguration.INSTANCE.getMethods().getText(webElement);
    }

    @Override
    public boolean check() {
        return content != null && !content.isEmpty();
    }
}
