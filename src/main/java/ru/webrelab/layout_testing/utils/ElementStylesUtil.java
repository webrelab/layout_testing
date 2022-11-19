package ru.webrelab.layout_testing.utils;

import com.google.gson.Gson;
import ru.webrelab.layout_testing.LayoutConfiguration;
import ru.webrelab.layout_testing.LayoutTestingException;
import ru.webrelab.layout_testing.snippets.Snippet;
import ru.webrelab.layout_testing.snippets.SnippetsRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ElementStylesUtil {

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getStyles(final Object webElement) {
        final List<Map<String, Object>> data = (List<Map<String, Object>>) LayoutConfiguration.INSTANCE.getFrameworkBasedBehavior().jsExecutor(
                Snippet.GET_STYLES, webElement);
        return data.stream()
                .filter(m -> m.size() == 2)
                .collect(Collectors.toMap(m -> {
                    String s = (String) m.get("name");
                    if (s == null) throw new LayoutTestingException("Key is wrong:\n" + new Gson().toJson(m));
                    return s;
                }, m -> {
                    Object s = m.get("value");
                    if (s == null) throw new LayoutTestingException("Value is wrong:\n" + new Gson().toJson(m));
                    return s;
                }, (a, b) -> b));
    }
}
