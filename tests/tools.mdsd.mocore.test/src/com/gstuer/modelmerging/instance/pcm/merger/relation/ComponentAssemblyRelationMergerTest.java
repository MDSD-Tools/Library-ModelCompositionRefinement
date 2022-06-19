package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.RelationMergerTest;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAssemblyRelation;
import com.gstuer.modelmerging.test.utility.IdentifierGenerator;

public class ComponentAssemblyRelationMergerTest extends RelationMergerTest<ComponentAssemblyRelationMerger,
        PcmSurrogate, ComponentAssemblyRelation, Component, Component> {
    @Override
    protected ComponentAssemblyRelation createRelation(Component source, Component destination,
            boolean isPlaceholder) {
        return new ComponentAssemblyRelation(source, destination, isPlaceholder);
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
    protected Component getUniqueNonPlaceholderDestinationEntity() {
        return getUniqueNonPlaceholderSourceEntity();
    }

    @Override
    protected Component getPlaceholderOfDestinationEntity(Component destination) {
        return getPlaceholderOfSourceEntity(destination);
    }

    @Override
    protected ComponentAssemblyRelationMerger createMerger(PcmSurrogate model) {
        return new ComponentAssemblyRelationMerger(model);
    }

    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }
}
