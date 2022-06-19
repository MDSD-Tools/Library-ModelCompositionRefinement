package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.RelationMergerTest;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Interface;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceRequirementRelation;
import com.gstuer.modelmerging.test.utility.IdentifierGenerator;

public class InterfaceRequirementRelationMergerTest extends RelationMergerTest<InterfaceRequirementRelationMerger,
        PcmSurrogate, InterfaceRequirementRelation, Component, Interface> {
    @Override
    protected InterfaceRequirementRelation createRelation(Component source, Interface destination,
            boolean isPlaceholder) {
        return new InterfaceRequirementRelation(source, destination, isPlaceholder);
    }

    @Override
    protected Component getUniqueNonPlaceholderSourceEntity() {
        return new Component(IdentifierGenerator.getUniqueIdentifier(), false);
    }

    @Override
    protected Component getPlaceholderOfSourceEntity(Component source) {
        return new Component(source.getValue(), true);
    }

    @Override
    protected Interface getUniqueNonPlaceholderDestinationEntity() {
        return new Interface(IdentifierGenerator.getUniqueIdentifier(), false);
    }

    @Override
    protected Interface getPlaceholderOfDestinationEntity(Interface destination) {
        return new Interface(destination.getValue(), true);
    }

    @Override
    protected InterfaceRequirementRelationMerger createMerger(PcmSurrogate model) {
        return new InterfaceRequirementRelationMerger(model);
    }

    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }
}
