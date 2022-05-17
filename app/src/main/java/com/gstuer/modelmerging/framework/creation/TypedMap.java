package com.gstuer.modelmerging.framework.creation;

import java.util.HashMap;
import java.util.Map;

public class TypedMap {
    private final Map<Class<?>, Object> data;

    public TypedMap() {
        this.data = new HashMap<>();
    }

    public <T> void put(T element) {
        this.data.put(element.getClass(), element);
    }

    public <T> T get(Class<T> key) {
        return key.cast(this.data.get(key));
    }
}
