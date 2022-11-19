package ru.webrelab.layout_testing.ifaces;

import ru.webrelab.layout_testing.snippets.Snippet;
import ru.webrelab.layout_testing.snippets.SnippetsRepository;

import java.util.Map;

public interface IFrameworkBasedBehavior {
    Object jsExecutor(String js, Object webElement);
    default Object jsExecutor(ISnippetEnum snippetEnum, Object webElement) {
        final String js = SnippetsRepository.INSTANCE.getSnippet(snippetEnum);
        return jsExecutor(js, webElement);
    }
    default Object jsExecutor(Map<String, String> placeholderReplaceMap, ISnippetEnum snippetEnum, Object webElement) {
        String js = SnippetsRepository.INSTANCE.getSnippet(snippetEnum);
        for (Map.Entry<String, String> e : placeholderReplaceMap.entrySet()) {
            js = js.replace(e.getKey(), e.getValue());
        }
        return jsExecutor(js, webElement);
    }
}
