package com.gstuer.modelmerging.framework.surrogate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TypedDistinctMultiMap<S extends Object> implements Iterable<S> {
    private final Map<Class<?>, Set<S>> data;

    public TypedDistinctMultiMap() {
        this.data = new HashMap<>();
    }

    public void put(S element) {
        Set<S> elements = this.data.getOrDefault(element.getClass(), new HashSet<>());
        elements.add(element);
        this.data.put(element.getClass(), elements);
    }

    public <T extends S> List<T> get(Class<T> key) {
        Set<S> objects = this.data.getOrDefault(key, new HashSet<>());
        return objects.stream().map(key::cast).collect(Collectors.toList());
    }

    public void remove(S element) {
        Set<S> elements = this.data.get(element.getClass());
        if (Objects.nonNull(elements)) {
            elements.remove(element);
        }
    }

    public boolean containsKey(Class<?> key) {
        if (Objects.isNull(key)) {
            return false;
        }
        return this.data.containsKey(key);
    }

    public boolean containsElement(S element) {
        if (Objects.isNull(element)) {
            return false;
        }
        Set<S> elements = this.data.getOrDefault(element.getClass(), new HashSet<>());
        return elements.contains(element);
    }

    @Override
    public Iterator<S> iterator() {
        Set<S> values = this.data.values().stream().flatMap(Set::stream).collect(Collectors.toUnmodifiableSet());
        return values.iterator();
    }
}
