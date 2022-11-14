package ru.webrelab.layout_testing.snippets;

import ru.webrelab.layout_testing.LayoutTestingException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SnippetsRepository {
    public static final SnippetsRepository INSTANCE = new SnippetsRepository();
    private final Map<String, String> resources = new HashMap<>();

    public String getSnippet(final String name) {
        return resources.get(name);
    }
    private SnippetsRepository() {
        Stream.of(Snippet.values()).map(Snippet::getSnippet).forEach(name -> {
            try(InputStream is = getClass().getClassLoader().getResourceAsStream("js_snippets/" + name)) {
                if (is == null) {
                    throw new LayoutTestingException(String.format("Error when reading file 'js_snippets/%s'. Check file name.", name));
                }
                InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(is));
                BufferedReader br = new BufferedReader(reader);
                resources.put(name, br.lines().collect(Collectors.joining("\n")));
            } catch (IOException e) {
                throw new LayoutTestingException("Error when reading file js_snippets/" + name);
            }
        });
    }
}
