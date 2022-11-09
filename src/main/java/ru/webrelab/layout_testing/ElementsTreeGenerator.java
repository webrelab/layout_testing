package ru.webrelab.layout_testing;

import ru.webrelab.layout_testing.snippets.Snippet;
import ru.webrelab.layout_testing.snippets.SnippetsRepository;

import java.util.*;

public class ElementsTreeGenerator extends HashMap<String, Object> {
    private final Map<String, List<String>> parentToChildren = new HashMap<>();

    @SuppressWarnings("unchecked")
    public Tree<String> createTree() {
        final List<Map<String, String>> response = (List<Map<String, String>>) LayoutConfiguration.INSTANCE.getMethods().executeJs(
                SnippetsRepository.INSTANCE.getSnippet(Snippet.CREATE_TREE.getSnippet()),
                this
        );
        for (final Map<String, String> e : response) {
            parentToChildren.compute(e.get("parent"), (k, v) -> {
                if (v == null) return new ArrayList<>();
                v.add(e.get("child"));
                return v;
            });
        }
        final String root = parentToChildren.entrySet()
                .stream()
                .max(Comparator.comparingInt(a -> a.getValue().size()))
                .orElseThrow(() -> new LayoutTestingException("Elements root not found"))
                .getKey();
        final Tree<String> rootTree = new Tree<>(root);
        fillTree(rootTree);
        rootTree.removeDuplicateChildren();
        return rootTree;
    }

    private void fillTree(final Tree<String> rootTree) {
        parentToChildren.get(rootTree.getValue()).forEach(ch -> new Tree<>(ch, rootTree));
        rootTree.getChildren().forEach(this::fillTree);
    }
}
