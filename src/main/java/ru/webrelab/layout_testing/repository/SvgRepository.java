package ru.webrelab.layout_testing.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import ru.webrelab.layout_testing.LayoutConfiguration;
import ru.webrelab.layout_testing.ifaces.IMethodsInjection;
import ru.webrelab.layout_testing.ifaces.IRepository;
import ru.webrelab.layout_testing.screen_difference.DifferentElements;
import ru.webrelab.layout_testing.snippets.Snippet;
import ru.webrelab.layout_testing.utils.ElementAttributesUtil;

import java.util.*;

@Getter
public class SvgRepository extends AttributeRepository {
    private static final IMethodsInjection methods = LayoutConfiguration.INSTANCE.getMethodsInjection();
    private final String fill;
    private final List<Map<String, Object>> vectors = new ArrayList<>();

    public SvgRepository(final String fill, final List<Map<String, Object>> vectors) {
        this.fill = fill;
        this.vectors.addAll(vectors);
    }

    public SvgRepository(final Object webElement) {
        final String fill = (String) ElementAttributesUtil.getStyles(webElement).get("fill");
        this.fill = fill == null ? "" : fill;
        updateVectors(webElement);
    }

    @SuppressWarnings("unchecked")
    public void updateVectors(final Object webElement) {
        final List<Map<String, Object>> response = (List<Map<String, Object>>) LayoutConfiguration.INSTANCE.getFrameworkBasedBehavior()
                .jsExecutor(Snippet.SVG_SCAN, webElement);
        response.forEach(line -> {
            for (final String s : new HashSet<>(line.keySet())) {
                if (line.get(s) == null) line.remove(s);
            }
        });
        vectors.addAll(response);
    }


    @Override
    public boolean check() {
        return !vectors.isEmpty();
    }

    @Override
    public List<DifferentElements> compareWith(IRepository expected) {
        if (!getClass().equals(expected.getClass())) {
            throw new IllegalArgumentException(String.format("Expected class '%s' doesn't equals actual class '%s'", expected.getClass().getName(), getClass().getName()));
        }
        final List<DifferentElements> differentElements = new ArrayList<>();
        if (!fill.equals(((SvgRepository) expected).getFill())) {
            differentElements.add(
                    new DifferentElements("fill", fill, ((SvgRepository) expected).getFill())
            );
        }
        final Iterator<Map<String, Object>> actualIterator = vectors.iterator();
        final Iterator<Map<String, Object>> expectedIterator = ((SvgRepository) expected).getVectors().iterator();
        while (actualIterator.hasNext() && expectedIterator.hasNext()) {
            final Map<String, Object> actualMap = actualIterator.next();
            final Map<String, Object> expectedMap = expectedIterator.next();
            if (!actualMap.equals(expectedMap)) {
                final Gson gson = new GsonBuilder().setPrettyPrinting().create();
                differentElements.add(
                        new DifferentElements("svg attributes", "\n" + gson.toJson(actualMap), "\n" + gson.toJson(expectedMap))
                );
                return differentElements;
            }
        }
        if (actualIterator.hasNext() || expectedIterator.hasNext()) {
            final String state = actualIterator.hasNext() ? "has extra elements" : "lost some elements";
            differentElements.add(
                    new DifferentElements("svg attributes", state, "")
            );
        }
        return differentElements;
    }
}
