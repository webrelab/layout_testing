package ru.webrelab.layout_testing.snippets;

import ru.webrelab.layout_testing.ifaces.ISnippetEnum;

public enum Snippet implements ISnippetEnum {
    CREATE_TREE("create_tree.js"),
    CREATE_CONTAINER("create_container.js"),
    GREED_DRAW("greed_draw.js"),
    MEASURE_TEXT("measure_text.js"),
    MEASURE_BEFORE_AFTER("measure_before_after.js"),
    MEASURE_DECOR("measure_decor.js"),
    GET_STYLES("get_styles.js"),
    GET_VIEWPORT_SIZE("get_viewport_size.js"),
    SVG_SCAN("svg_scan.js"),
    ;
    private final String snippet;

    Snippet(String snippet) {
        this.snippet = snippet;
    }

    public String getSnippet() {
        return snippet;
    }
}
