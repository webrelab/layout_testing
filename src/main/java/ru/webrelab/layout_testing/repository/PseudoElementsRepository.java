package ru.webrelab.layout_testing.repository;

import lombok.Getter;
import ru.webrelab.layout_testing.LayoutConfiguration;
import ru.webrelab.layout_testing.snippets.Snippet;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class PseudoElementsRepository extends AttributeRepository {
    private transient final Map<String, Object> data = new HashMap<>();

    @SuppressWarnings("unchecked")
    public PseudoElementsRepository(final Object webElement, final String type) {
        try {
            final Map<String, Map<String, Object>> styles = (Map<String, Map<String, Object>>) LayoutConfiguration.INSTANCE.getFrameworkBasedBehavior()
                    .jsExecutor(Snippet.PSEUDO_ELEMENTS_ATTR, webElement);
            data.putAll(styles.get(type));
        } catch (Throwable e) {
            System.out.println(type);
        }
    }

    @Override
    public PositionRepository getPosition(PositionRepository container, Object webElement) {
        final PositionRepository elementPosition = LayoutConfiguration.INSTANCE.getMethodsInjection().getPosition(container, webElement);
        int top = ((Number) data.get("topOffset")).intValue() + elementPosition.getTop();
        int left = ((Number) data.get("leftOffset")).intValue() + elementPosition.getLeft();
        return new PositionRepository(
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
        return !data.get("content").equals("none")
                && ((Number) data.get("height")).intValue() != 0
                && ((Number) data.get("width")).intValue() != 0;
    }

    @Override
    public String getTransform(Object webElement) {
        return (String) data.get("transform");
    }
}
