package com.gstuer.modelmerging.instance.pcm.surrogate.relation;

import com.gstuer.modelmerging.framework.surrogate.RelationTest;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Interface;

public class ComponentAssemblyRelationTest
        extends RelationTest<ComponentAssemblyRelation, InterfaceProvisionRelation, InterfaceRequirementRelation> {
    @Override
    protected ComponentAssemblyRelation createRelation(InterfaceProvisionRelation source,
            InterfaceRequirementRelation destination,
            boolean isPlaceholder) {
        return new ComponentAssemblyRelation(source, destination, isPlaceholder);
    }

    @Override
    protected InterfaceProvisionRelation getUniqueSourceEntity() {
        Component source = Component.getUniquePlaceholder();
        Interface destination = Interface.getUniquePlaceholder();
        return new InterfaceProvisionRelation(source, destination, true);
    }

    @Override
    protected InterfaceRequirementRelation getUniqueDestinationEntity() {
        Component source = Component.getUniquePlaceholder();
        Interface destination = Interface.getUniquePlaceholder();
        return new InterfaceRequirementRelation(source, destination, true);
    }
}
