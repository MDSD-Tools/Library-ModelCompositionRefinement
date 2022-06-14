package com.gstuer.modelmerging.instance.pcm.surrogate.relation;

import com.gstuer.modelmerging.framework.surrogate.Relation;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Interface;

public class ProvidesRelation extends Relation<Component, Interface> {
    public ProvidesRelation(Component source, Interface destination, boolean isPlaceholder) {
        super(source, destination, isPlaceholder);
    }
}
