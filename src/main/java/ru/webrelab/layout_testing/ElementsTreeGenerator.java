package ru.webrelab.layout_testing;

import com.google.gson.Gson;
import ru.webrelab.layout_testing.ifaces.IMeasuringType;
import ru.webrelab.layout_testing.snippets.Snippet;

import java.util.*;
import java.util.stream.Collectors;

public class ElementsTreeGenerator extends HashMap<String, Object> {
    private final Map<String, String> childToParentCleaned = new HashMap<>();
    private final LayoutCollection layoutCollection;

    public ElementsTreeGenerator(final LayoutCollection layoutCollection) {
        this.layoutCollection = layoutCollection;
        layoutCollection.forEach((uuid, el) -> put(uuid, el.getElement()));
    }

    @SuppressWarnings("unchecked")
    public void createTree() {
        final List<Map<String, String>> response =
                (List<Map<String, String>>) LayoutConfiguration.INSTANCE
                        .getFrameworkBasedBehavior()
                        .jsExecutor(Snippet.CREATE_TREE, this);
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
        final TreeFilter filter = new TreeFilter(childToParentPairs, layoutCollection);
        childToParentCleaned.putAll(filter.filter());
    }

    public void updateLayoutElements() {
        childToParentCleaned.forEach((child, parent) -> {
            layoutCollection.get(child).setParent(parent);
            layoutCollection.get(parent).addChild(child);
        });
        checkWrongDependency(layoutCollection);
    }

    private void checkWrongDependency(final LayoutCollection layoutElements) {
        for (final LayoutElement e : layoutElements.values()) {
            if (e.getChildren().contains(e.getParent())) {
                throw new LayoutTestingException("Element found with same parent and children:\n" + new Gson().toJson(e));
            }
        }
    }

    public static class TreeFilter {
        private final Map<String, List<String>> childToParentsMap;
        private final LayoutCollection layoutCollection;

        public TreeFilter(Map<String, List<String>> childToParentsMap, final LayoutCollection layoutCollection) {
            this.layoutCollection = layoutCollection;
            this.childToParentsMap = childToParentsMap.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Entry::getKey, e -> new ArrayList<>(e.getValue()), (a, b) -> b, HashMap::new));
        }

        public Map<String, String> filter() {
            final Map<String, String> map = new HashMap<>();
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
                // If there are no single-parent records left, then the list contains elements with multiple parents.
                // In this case, one parent is selected according to the principle - who is higher in the enum IMeasuringType list
                if (currentMapSize == childToParentsMap.size()) {
                    final Set<String> parentsForRemoving = new HashSet<>();
                    childToParentsMap.forEach((child, parents) -> {
                        parentsForRemoving.addAll(parents);
                        final String firstParent = parents
                                .stream()
                                .min((a, b) -> compareMeasuringTypes(layoutCollection.get(a).getType(), layoutCollection.get(b).getType()))
                                .orElseThrow(() -> new LayoutTestingException("Parents list can't be empty"));
                        map.put(child, firstParent);
                    });
                    parentsForRemoving.forEach(this::cleanParents);
                    cleanChildren();
                }
            }
            return map;
        }

        private int compareMeasuringTypes(final IMeasuringType type1, final IMeasuringType type2) {
            return ((Enum<?>) type1).ordinal() - ((Enum<?>) type2).ordinal();
        }

        private void cleanChildren() {
            for (final String child : new ArrayList<>(childToParentsMap.keySet())) {
                if (childToParentsMap.get(child).isEmpty()) childToParentsMap.remove(child);
            }
        }

        private void cleanParents(final String parent) {
            for (final String child : childToParentsMap.keySet()) {
                childToParentsMap.get(child).removeIf(parent::equals);
            }
        }
    }
}
