package com.gstuer.modelmerging.instance.pcm.surrogate.relation;

import com.gstuer.modelmerging.framework.surrogate.RelationTest;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;

public class ComponentAssemblyRelationTest extends RelationTest<ComponentAssemblyRelation, Component, Component> {
    @Override
    protected ComponentAssemblyRelation createRelation(Component source, Component destination,
            boolean isPlaceholder) {
        return new ComponentAssemblyRelation(source, destination, isPlaceholder);
    }

    @Override
    protected Component getUniqueSourceEntity() {
        return Component.getUniquePlaceholder();
    }

    @Override
    protected Component getUniqueDestinationEntity() {
        return Component.getUniquePlaceholder();
    }
}
