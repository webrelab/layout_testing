package ru.webrelab.layout_testing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.webrelab.layout_testing.ElementsTreeGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeFilterTest {

    @Test
    void filter1() {
        final ElementsTreeGenerator.TreeFilter filter = new ElementsTreeGenerator.TreeFilter(new HashMap<>(), null);
        Assertions.assertEquals(new HashMap<>(), filter.filter());
    }

    @Test
    void filter2() {
        Map<String, List<String>> map = Map.of(
                "2", List.of("1")
        );
        Map<String, String> expected = Map.of("2", "1");
        final ElementsTreeGenerator.TreeFilter filter = new ElementsTreeGenerator.TreeFilter(map, null);
        Assertions.assertEquals(expected, filter.filter());
    }

    @Test
    void filter3() {
        Map<String, List<String>> map = Map.of(
                "7", List.of("8"),
                "9", List.of("8")
        );

        Map<String, String> expected = Map.of(
                "7", "8",
                "9", "8"
        );
        final ElementsTreeGenerator.TreeFilter filter = new ElementsTreeGenerator.TreeFilter(map, null);
        Assertions.assertEquals(expected, filter.filter());
    }

    @Test
    void filter4() {
        Map<String, List<String>> map = Map.of(
                "3", List.of("1"),
                "2", List.of("1"),
                "4", List.of("1", "2"),
                "5", List.of("1", "2")
        );
        Map<String, String> expected = Map.of(
                "3", "1",
                "2", "1",
                "4", "2",
                "5", "2"
        );
        final ElementsTreeGenerator.TreeFilter filter = new ElementsTreeGenerator.TreeFilter(map, null);
        final Map<String, String> result = filter.filter();
        printTree(result);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void filter5() {
        Map<String, List<String>> map = Map.of(
                "3", List.of("1"),
                "2", List.of("1"),
                "4", List.of("1", "2"),
                "5", List.of("1", "2"),
                "7", List.of("8"),
                "9", List.of("8")
        );
        Map<String, String> expected = Map.of(
                "3", "1",
                "2", "1",
                "4", "2",
                "5", "2",
                "7", "8",
                "9", "8"
        );
        final ElementsTreeGenerator.TreeFilter filter = new ElementsTreeGenerator.TreeFilter(map, null);
        Assertions.assertEquals(expected, filter.filter());
    }

    @Test
    void filter6() {
        Map<String, List<String>> map = Map.of(
                "9", List.of("8", "6", "2", "1"),
                "8", List.of("6", "2", "1"),
                "7", List.of("6", "2", "1"),
                "5", List.of("2", "1"),
                "6", List.of("2", "1"),
                "3", List.of("1"),
                "4", List.of("1"),
                "2", List.of("1")
        );
        Map<String, String> expected = Map.of(
                "9", "8",
                "8", "6",
                "7", "6",
                "6", "2",
                "5", "2",
                "2", "1",
                "3", "1",
                "4", "1"
        );
        final ElementsTreeGenerator.TreeFilter filter = new ElementsTreeGenerator.TreeFilter(map, null);
        final Map<String, String> result = filter.filter();
        printTree(result);
        Assertions.assertEquals(expected, result);
    }


    private void printTree(final Map<String, String> childToParentCleaned) {
        final Map<String, List<String>> parentToChildrenMap = new HashMap<>();
        childToParentCleaned.forEach((ch, p) ->
                parentToChildrenMap.compute(p, (parent, children) -> {
                    if (children == null) return new ArrayList<>(List.of(ch));
                    children.add(ch);
                    return children;
                })
        );
        parentToChildrenMap.forEach((p, ch) -> {
            System.out.print(p + ": ");
            System.out.println(String.join(", ", ch));
        });
    }
}
