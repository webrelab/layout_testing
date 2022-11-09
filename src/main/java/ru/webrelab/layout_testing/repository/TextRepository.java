package ru.webrelab.layout_testing.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.webrelab.layout_testing.LayoutConfiguration;

import java.util.Map;

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

    @SuppressWarnings("unchecked")
    public TextRepository(final Object object) {
        final Map<String, Object> styles =
                (Map<String, Object>) LayoutConfiguration.INSTANCE.getMethods().executeJs("return window.getComputedStyle(arguments[0])", object);
        fontFamily = (String) styles.get("fontFamily");
        fontSize = (String) styles.get("fontSize");
        fontWeight = (String) styles.get("fontWeight");
        color = (String) styles.get("color");
        fontStyle = (String) styles.get("fontStyle");
        fontVariant = (String) styles.get("fontVariant");
        textDecoration = (String) styles.get("textDecoration");
        textShadow = (String) styles.get("textShadow");
        content = LayoutConfiguration.INSTANCE.getMethods().getText(object);

    }
}
