package ru.webrelab.layout_testing.repository;

import lombok.SneakyThrows;
import ru.webrelab.layout_testing.screen_difference.DifferentElements;
import ru.webrelab.layout_testing.ifaces.IRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

abstract class Repository implements IRepository {

    @SneakyThrows
    @Override
    public Map<String, Object> getFieldMap() {
        final Field[] fields = getClass().getDeclaredFields();
        final Map<String, Object> map = new HashMap<>();
        for (final Field field : fields) {
            map.put(field.getName(), field.get(this));
        }
        return map;
    }

    protected List<DifferentElements> compareWith(IRepository expected, BiFunction<Object, Object, Boolean> predicate) {
        if (!getClass().equals(expected.getClass())) {
            throw new IllegalArgumentException(String.format("Expected class '%s' doesn't equals actual class '%s'", expected.getClass().getName(), getClass().getName()));
        }
        final List<DifferentElements> result = new ArrayList<>();
        final Map<String, Object> expectedData = expected.getFieldMap();
        for (final Map.Entry<String, Object> actual : getFieldMap().entrySet()) {
            if (predicate.apply(actual.getValue(), expectedData.get(actual.getKey()))) {
                result.add(new DifferentElements(actual.getKey(), actual.getValue(), expectedData.get(actual.getKey())));
            }
        }
        return result;
    }
}
