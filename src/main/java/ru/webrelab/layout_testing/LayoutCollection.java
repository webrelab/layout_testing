package ru.webrelab.layout_testing;

import java.util.HashMap;

public class LayoutCollection extends HashMap<String, LayoutElement> {
    public void add(final LayoutElement element) {
        put(element.getId(), element);
    }
}
