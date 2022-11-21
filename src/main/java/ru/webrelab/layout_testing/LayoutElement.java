package ru.webrelab.layout_testing;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;
import ru.webrelab.layout_testing.ifaces.IMeasuringType;
import ru.webrelab.layout_testing.ifaces.IRepository;
import ru.webrelab.layout_testing.repository.PositionRepository;
import ru.webrelab.layout_testing.repository.SizeRepository;
import ru.webrelab.layout_testing.utils.EnumDetermination;

import java.util.*;

@Getter
public class LayoutElement {
    private final String id;
    private final String name;
    private boolean ignore = false;
    private final String tagName;
    private final IMeasuringType type;
    private final PositionRepository position;
    private final SizeRepository size;
    private final IRepository data;
    private final String transform;
    private transient final Object element;
    private String parent = "";
    private Set<String> children = new HashSet<>();

    public LayoutElement(
            String name,
            String tagName,
            final IMeasuringType type,
            PositionRepository position,
            SizeRepository size,
            IRepository data,
            String transform,
            Object element
    ) {
        id = IdGen.getNew();
        this.name = name;
        this.tagName = tagName;
        this.type = type;
        this.data = data;
        this.transform = transform;
        this.element = element;
        this.position = position;
        this.size = size;
    }

    @SuppressWarnings("unchecked")
    public LayoutElement(final JsonObject jsonObject) {
        final Gson gson = new Gson();
        id = jsonObject.getAsJsonPrimitive("id").getAsString();
        name = jsonObject.getAsJsonPrimitive("name").getAsString();
        ignore = jsonObject.getAsJsonPrimitive("ignore").getAsBoolean();
        tagName = jsonObject.getAsJsonPrimitive("tagName").getAsString();
        final String typeName = jsonObject.getAsJsonPrimitive("type").getAsString();
        type = EnumDetermination.determineMeasureEnum(typeName);
        position = gson.fromJson(jsonObject.getAsJsonObject("position"), PositionRepository.class);
        size = gson.fromJson(jsonObject.getAsJsonObject("size"), SizeRepository.class);
        data = gson.fromJson(jsonObject.getAsJsonObject("data"), type.getRepositoryClass());
        transform = jsonObject.getAsJsonPrimitive("transform").getAsString();
        element = null;
        parent = jsonObject.getAsJsonPrimitive("parent").getAsString();
        children = gson.fromJson(jsonObject.getAsJsonArray("children"), Set.class);
    }

    public boolean compareSignature(final LayoutElement expected) {
        return name.equals(expected.name) &&
                type == expected.getType();
    }

    public int getDimensionViolation(final LayoutElement expected) {
        return Math.max(
                position.getViolationWith(expected.getPosition()),
                size.getViolationWith(expected.getSize())
        );
    }

    public void setParent(final String parent) {
        this.parent = parent;
    }

    public void addChild(final String child) {
        children.add(child);
    }

    static class IdGen {
        private static int counter = 0;
        static String getNew() {
            return String.valueOf(++counter);
        }
    }
}
