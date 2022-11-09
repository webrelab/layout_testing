package ru.webrelab.layout_testing.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import ru.webrelab.layout_testing.LayoutElement;

import java.util.ArrayList;
import java.util.List;

public class DataTransformer {
    public static String serialize(final List<LayoutElement> data) {
        return new Gson().toJson(data);
    }

    public static List<LayoutElement> deserialize(final String json) {
        final JsonElement array = JsonParser.parseString(json);
        final List<LayoutElement> layoutElements = new ArrayList<>();
        if (!array.isJsonArray()) return layoutElements;
        for (final JsonElement element : array.getAsJsonArray()) {
            layoutElements.add(new LayoutElement(element.getAsJsonObject()));
        }
        return layoutElements;
    }
}
