package com.gstuer.modelmerging.framework.creation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TypedDistinctMultiMap {
    private final Map<Class<?>, Set<Object>> data;

    public TypedDistinctMultiMap() {
        this.data = new HashMap<>();
    }

    public <T> void put(T element) {
        Set<Object> elements = this.data.getOrDefault(element.getClass(), new HashSet<>());
        elements.add(element);
        this.data.put(element.getClass(), elements);
    }

    public <T> List<T> get(Class<T> key) {
        Set<Object> objects = this.data.getOrDefault(key, new HashSet<>());
        return objects.stream().map(key::cast).collect(Collectors.toList());
    }
}
