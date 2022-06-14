package com.gstuer.modelmerging.instance.pcm.surrogate.relation;

import com.gstuer.modelmerging.framework.surrogate.Relation;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Interface;

public class RequiresRelation extends Relation<Component, Interface> {
    public RequiresRelation(Component source, Interface destination, boolean isPlaceholder) {
        super(source, destination, isPlaceholder);
    }
}
