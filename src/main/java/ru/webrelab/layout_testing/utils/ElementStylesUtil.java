package ru.webrelab.layout_testing.utils;

import ru.webrelab.layout_testing.LayoutConfiguration;
import ru.webrelab.layout_testing.snippets.Snippet;
import ru.webrelab.layout_testing.snippets.SnippetsRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ElementStylesUtil {

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getStyles(final Object webElement) {
        final List<Map<String, Object>> data = (List<Map<String, Object>>) LayoutConfiguration.INSTANCE.getMethods().executeJs(
                SnippetsRepository.INSTANCE.getSnippet(Snippet.GET_STYLES.getSnippet()), webElement);
        return data.stream()
                .collect(Collectors.toMap(m -> (String) m.get("name"), m -> m.get("value"), (a, b) -> b));
    }
}
