package com.gstuer.modelmerging.instance.pcm.transformation;

import org.palladiosimulator.generator.fluent.allocation.api.IAllocationAddition;
import org.palladiosimulator.generator.fluent.allocation.factory.FluentAllocationFactory;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

import com.gstuer.modelmerging.framework.transformation.Transformer;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;

public class AllocationTransformer implements Transformer<PcmSurrogate, Allocation> {
    @Override
    public Allocation transform(PcmSurrogate model) {
        FluentAllocationFactory allocationFactory = new FluentAllocationFactory();
        System system = new SystemTransformer().transform(model);
        ResourceEnvironment resourceEnvironment = new ResourceEnvironmentTransformer().transform(model);
        IAllocationAddition fluentAllocation = allocationFactory.newAllocation()
                .withSystem(system)
                .withResourceEnvironment(resourceEnvironment);

        // TODO Add assembly context (Component) <=> resource container (Deployment) relations

        return fluentAllocation.createAllocationNow();
    }
}
