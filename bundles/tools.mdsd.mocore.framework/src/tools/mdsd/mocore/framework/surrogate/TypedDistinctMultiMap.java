/* Copyright (C) 2023 Moritz Gstuer - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the EUPL 1.2 license.
 *
 * You should have received a copy of the EUPL 1.2 license
 * with this file. If not, please visit:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */
package tools.mdsd.mocore.framework.surrogate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TypedDistinctMultiMap<S extends Object> implements Iterable<S> {
    private static final boolean DEFAULT_IGNORE_INHERITANCE = false;

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
        return get(key, DEFAULT_IGNORE_INHERITANCE);
    }

    @SuppressWarnings("unchecked")
    public <T extends S> List<T> get(Class<T> key, boolean ignoreInheritance) {
        // Get all keys that are children of key
        Set<Class<T>> keys = new HashSet<>();
        keys.add(key);
        if (!ignoreInheritance) {
            for (Class<?> childKey : this.data.keySet()) {
                if (key.isAssignableFrom(childKey)) {
                    keys.add((Class<T>) childKey);
                }
            }
        }

        // Retrieve elements for all fitting keys
        Set<S> objects = new HashSet<>();
        for (Class<T> childKey : keys) {
            objects.addAll(this.data.getOrDefault(childKey, new HashSet<>()));
        }
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
