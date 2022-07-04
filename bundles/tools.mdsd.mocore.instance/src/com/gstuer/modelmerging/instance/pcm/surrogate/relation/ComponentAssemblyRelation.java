package com.gstuer.modelmerging.instance.pcm.surrogate.relation;

import com.gstuer.modelmerging.framework.surrogate.Relation;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;

public class ComponentAssemblyRelation extends Relation<InterfaceProvisionRelation, InterfaceRequirementRelation> {
    public ComponentAssemblyRelation(InterfaceProvisionRelation source, InterfaceRequirementRelation destination,
            boolean isPlaceholder) {
        super(source, destination, isPlaceholder);
    }

    @Override
    public <U extends Replaceable> ComponentAssemblyRelation replace(U original, U replacement) {
        if (!this.canReplace(original)) {
            // TODO Add message to exception
            throw new IllegalArgumentException();
        }
        if (this.equals(original)) {
            return (ComponentAssemblyRelation) replacement;
        }
        InterfaceProvisionRelation source = getSourceReplacement(original, replacement);
        InterfaceRequirementRelation destination = getDestinationReplacement(original, replacement);
        return new ComponentAssemblyRelation(source, destination, this.isPlaceholder());
    }
}
