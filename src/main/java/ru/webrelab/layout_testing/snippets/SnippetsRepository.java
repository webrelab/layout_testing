package ru.webrelab.layout_testing.snippets;

import ru.webrelab.layout_testing.LayoutTestingException;
import ru.webrelab.layout_testing.ifaces.ISnippetEnum;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SnippetsRepository {
    public static final SnippetsRepository INSTANCE = new SnippetsRepository();
    private final Map<ISnippetEnum, String> resources = new HashMap<>();

    public String getSnippet(final ISnippetEnum snippetName) {
        if (!resources.containsKey(snippetName)) loadJsSnippet(snippetName);
        return resources.get(snippetName);
    }

    private void loadJsSnippet(final ISnippetEnum snippetName) {
        try(InputStream is = getClass().getClassLoader().getResourceAsStream("js_snippets/" + snippetName.getSnippet())) {
            if (is == null) {
                throw new LayoutTestingException(String.format("Error when reading file 'js_snippets/%s'. Check file name.", snippetName));
            }
            InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(is));
            BufferedReader br = new BufferedReader(reader);
            resources.put(snippetName, br.lines().collect(Collectors.joining("\n")));
        } catch (IOException e) {
            throw new LayoutTestingException("Error when reading file js_snippets/" + snippetName);
        }
    }
}
