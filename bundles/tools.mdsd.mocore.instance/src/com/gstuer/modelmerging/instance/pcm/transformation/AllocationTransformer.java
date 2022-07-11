package com.gstuer.modelmerging.instance.pcm.transformation;

import java.util.List;

import org.palladiosimulator.generator.fluent.allocation.api.IAllocationAddition;
import org.palladiosimulator.generator.fluent.allocation.factory.FluentAllocationFactory;
import org.palladiosimulator.generator.fluent.allocation.structure.AllocationContextCreator;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

import com.gstuer.modelmerging.framework.transformation.Transformer;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAllocationRelation;

public class AllocationTransformer implements Transformer<PcmSurrogate, Allocation> {
    @Override
    public Allocation transform(PcmSurrogate model) {
        FluentAllocationFactory allocationFactory = new FluentAllocationFactory();
        System system = new SystemTransformer().transform(model);
        ResourceEnvironment resourceEnvironment = new ResourceEnvironmentTransformer().transform(model);
        IAllocationAddition fluentAllocation = allocationFactory.newAllocation()
                .withSystem(system)
                .withResourceEnvironment(resourceEnvironment);

        // Add allocation contexts to allocation
        List<ComponentAllocationRelation> relations = model.getByType(ComponentAllocationRelation.class);
        for (ComponentAllocationRelation relation : relations) {
            // Get and add context (creator) for specific allocation relation
            AllocationContextCreator contextCreator = getCreator(relation);
            fluentAllocation.addToAllocation(contextCreator);
        }

        return fluentAllocation.createAllocationNow();
    }

    private AllocationContextCreator getCreator(ComponentAllocationRelation relation) {
        AllocationContextCreator contextCreator = new FluentAllocationFactory().newAllocationContext();

        // Use name of entities to fetch up-to-date entities from system and resource environment
        String componentEntityName = relation.getSource().getValue().getEntityName();
        String deploymentEntityName = relation.getDestination().getValue().getEntityName();
        // Assembly context represents component in system view
        contextCreator.withAssemblyContext(componentEntityName).withResourceContainer(deploymentEntityName);

        return contextCreator;
    }
}
