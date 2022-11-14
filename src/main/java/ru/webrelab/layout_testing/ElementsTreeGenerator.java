package ru.webrelab.layout_testing;

import com.google.gson.Gson;
import ru.webrelab.layout_testing.snippets.Snippet;
import ru.webrelab.layout_testing.snippets.SnippetsRepository;

import java.util.*;
import java.util.stream.Collectors;

public class ElementsTreeGenerator extends HashMap<String, Object> {
    private final Map<String, String> childToParentCleaned = new HashMap<>();

    public ElementsTreeGenerator(final LayoutCollection layoutCollection) {
        layoutCollection.forEach((uuid, el) -> put(uuid, el.getElement()));
    }

    @SuppressWarnings("unchecked")
    public void createTree() {
        final List<Map<String, String>> response = (List<Map<String, String>>) LayoutConfiguration.INSTANCE.getMethods().executeJs(
                SnippetsRepository.INSTANCE.getSnippet(Snippet.CREATE_TREE.getSnippet()),
                this
        );
        final Map<String, List<String>> childToParentPairs = new HashMap<>();
        for (final Map<String, String> e : response) {
            final String parent = e.get("parent");
            final String child = e.get("child");
            childToParentPairs.compute(child, (k, v) -> {
                if (v == null) return new ArrayList<>(List.of(parent));
                v.add(parent);
                return v;
            });
        }
        final TreeFilter<String> filter = new TreeFilter<>(childToParentPairs);
        childToParentCleaned.putAll(filter.filter());
    }

    public void updateLayoutElements(final LayoutCollection layoutElements) {
        childToParentCleaned.forEach((child, parent) -> {
            layoutElements.get(child).setParent(parent);
            layoutElements.get(parent).addChild(child);
        });
        checkWrongDependency(layoutElements);
    }

    private void checkWrongDependency(final LayoutCollection layoutElements) {
        for (final LayoutElement e : layoutElements.values()) {
            if (e.getChildren().contains(e.getParent())) {
                throw new LayoutTestingException("Обнаружен элемент с одинаковым parent и children:\n" + new Gson().toJson(e));
            }
        }
    }

    public static class TreeFilter<T> {
        private final Map<T, List<T>> childToParentsMap;

        public TreeFilter(Map<T, List<T>> childToParentsMap) {
            this.childToParentsMap = childToParentsMap.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Entry::getKey, e -> new ArrayList<>(e.getValue()), (a, b) -> b, HashMap::new));
        }

        public Map<T, T> filter() {
            final Map<T, T> map = new HashMap<>();
            while (!childToParentsMap.isEmpty()) {
                childToParentsMap.entrySet()
                        .stream()
                        .filter(e -> e.getValue().size() == 1)
                        .forEach(e -> map.put(e.getKey(), e.getValue().get(0)));
                map.values()
                        .stream()
                        .distinct()
                        .forEach(this::cleanParents);
                int currentMapSize = childToParentsMap.size();
                cleanChildren();
                if (currentMapSize == childToParentsMap.size()) {
                    throw new LayoutTestingException("Children filter algorithm is working incorrect");
                }
            }
            return map;
        }

        private void cleanChildren() {
            for (final T child : new ArrayList<>(childToParentsMap.keySet())) {
                if (childToParentsMap.get(child).isEmpty()) childToParentsMap.remove(child);
            }
        }

        private void cleanParents(final T parent) {
            for (final T child : childToParentsMap.keySet()) {
                childToParentsMap.get(child).removeIf(parent::equals);
            }
        }
    }
}
