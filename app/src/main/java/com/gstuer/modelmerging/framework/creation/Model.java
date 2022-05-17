package com.gstuer.modelmerging.framework.creation;

import java.util.List;

public interface Model {
    <T> List<T> getElementsByType(Class<T> elementType);

    <T> void addElement(T element);

    <T> List<T> getRelationsByType(Class<T> relationType);

    <T> void addRelation(T relation);
}
