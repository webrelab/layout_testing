package ru.webrelab.layout_testing.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import ru.webrelab.layout_testing.LayoutCollection;
import ru.webrelab.layout_testing.LayoutElement;

public class DataTransformer {
    public static String serialize(final LayoutCollection data) {
        return new Gson().toJson(data.values());
    }

    public static LayoutCollection deserialize(final String json) {
        final JsonElement array = JsonParser.parseString(json);
        final LayoutCollection layoutElements = new LayoutCollection();
        if (!array.isJsonArray()) return layoutElements;
        for (final JsonElement element : array.getAsJsonArray()) {
            layoutElements.add(new LayoutElement(element.getAsJsonObject()));
        }
        return layoutElements;
    }
}
