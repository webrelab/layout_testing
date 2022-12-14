package ru.webrelab.layout_testing.ifaces;

import ru.webrelab.layout_testing.repository.SizeRepository;
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

    /**
     * The method is required to get the size of the web element
     * @param webElement web element
     * @return a SizeRepository object with the element's current dimensions
     */
    @SuppressWarnings("unchecked")
    default SizeRepository getSize(Object webElement) {
        final Map<String, Object> size = (Map<String, Object>) jsExecutor(Snippet.GET_ELEMENT_SIZE, webElement);
        return new SizeRepository(
                ((Number) size.get("height")).intValue(),
                ((Number) size.get("width")).intValue()
        );
    }
}
