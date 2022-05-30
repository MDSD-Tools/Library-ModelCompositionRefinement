package com.gstuer.modelmerging.instance.pcm.surrogate.relation;

import com.gstuer.modelmerging.framework.surrogate.Relation;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;

public class ComponentComponentRelation extends Relation<Component, Component> {
    public ComponentComponentRelation(Component source, Component destination, boolean isPlaceholder) {
        super(source, destination, isPlaceholder);
    }
}
