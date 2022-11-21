package ru.webrelab.layout_testing.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.webrelab.layout_testing.LayoutConfiguration;
import ru.webrelab.layout_testing.snippets.Snippet;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public abstract class PseudoElementsRepository extends AttributeRepository {
    private final Map<String, Object> data = new HashMap<>();

    @SuppressWarnings("unchecked")
    public PseudoElementsRepository(final Object webElement, final String type) {
        final Map<String, Map<String, Object>> styles = (Map<String, Map<String, Object>>) LayoutConfiguration.INSTANCE.getFrameworkBasedBehavior()
                .jsExecutor(Snippet.PSEUDO_ELEMENTS_ATTR, webElement);
        data.putAll(styles.get(type));
    }

    @Override
    public PositionRepository getPosition(PositionRepository container, Object webElement) {
        int top = ((Number) data.get("top")).intValue();
        int left = ((Number) data.get("left")).intValue();
        return new PositionRepository(
                container,
                top,
                left
        );
    }

    @Override
    public SizeRepository getSize(Object webElement) {
        int height = ((Number) data.get("height")).intValue();
        int width = ((Number) data.get("width")).intValue();
        return new SizeRepository(height, width);
    }

    @Override
    public boolean check() {
        return !data.get("content").equals("none");
    }

    @Override
    public String getTransform(Object webElement) {
        return (String) data.get("transform");
    }
}
