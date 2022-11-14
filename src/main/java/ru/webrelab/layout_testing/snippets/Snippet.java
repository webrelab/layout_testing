package ru.webrelab.layout_testing.snippets;

public enum Snippet {
    CREATE_TREE("create_tree.js_snippet"),
    CREATE_CONTAINER("create_container.js"),
    GREED_DRAW("greed_draw.js"),
    MEASURE_TEXT("measure_text.js"),
    MEASURE_BEFORE_AFTER("measure_before_after.js"),
    MEASURE_DECOR("measure_decor.js"),
    GET_STYLES("get_styles.js_snippet"),
    ;
    private final String snippet;

    Snippet(String snippet) {
        this.snippet = snippet;
    }

    public String getSnippet() {
        return snippet;
    }
}
