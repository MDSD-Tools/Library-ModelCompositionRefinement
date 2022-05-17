package com.gstuer.modelmerging.framework.surrogate;

import java.util.List;

public interface Model {
    <T extends Replaceable> List<T> getByType(Class<T> type);

    void add(Replaceable replaceable);
}
