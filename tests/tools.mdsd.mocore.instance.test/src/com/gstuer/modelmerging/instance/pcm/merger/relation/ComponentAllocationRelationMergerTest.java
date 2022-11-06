package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.RelationProcessorTest;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAllocationRelation;
import com.gstuer.modelmerging.instance.pcm.utility.ElementFactory;

public class ComponentAllocationRelationMergerTest extends RelationProcessorTest<ComponentAllocationRelationMerger,
        PcmSurrogate, ComponentAllocationRelation, Component, Deployment> {
    @Override
    protected ComponentAllocationRelation createRelation(Component source, Deployment destination,
            boolean isPlaceholder) {
        return new ComponentAllocationRelation(source, destination, isPlaceholder);
    }

    @Override
    protected Component getUniqueNonPlaceholderSourceEntity() {
        return ElementFactory.createUniqueComponent(false);
    }

    @Override
    protected Component getPlaceholderOfSourceEntity(Component source) {
        return new Component(source.getValue(), true);
    }

    @Override
    protected Deployment getUniqueNonPlaceholderDestinationEntity() {
        return ElementFactory.createUniqueDeployment(false);
    }

    @Override
    protected Deployment getPlaceholderOfDestinationEntity(Deployment destination) {
        return new Deployment(destination.getValue(), true);
    }

    @Override
    protected ComponentAllocationRelationMerger createProcessor(PcmSurrogate model) {
        return new ComponentAllocationRelationMerger(model);
    }

    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }
}
