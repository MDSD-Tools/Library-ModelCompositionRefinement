package com.gstuer.modelmerging.instance.pcm.transformation;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.palladiosimulator.pcm.system.System;

import com.gstuer.modelmerging.framework.transformation.TransformerTest;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Interface;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAssemblyRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceProvisionRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceRequirementRelation;

public class SystemTransformerTest extends TransformerTest<SystemTransformer, PcmSurrogate, System> {
    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void testTransformSingleAssembly(boolean isPlaceholderAssembly) {
        // Test data
        SystemTransformer transformer = createTransformer();
        PcmSurrogate model = createEmptyModel();

        Component provider = Component.getUniquePlaceholder();
        Component consumer = Component.getUniquePlaceholder();
        Interface providerConsumerInterface = Interface.getUniquePlaceholder();
        InterfaceProvisionRelation provisionRelation = new InterfaceProvisionRelation(provider,
                providerConsumerInterface, false);
        InterfaceRequirementRelation requirementRelation = new InterfaceRequirementRelation(consumer,
                providerConsumerInterface, false);
        ComponentAssemblyRelation assemblyRelation = new ComponentAssemblyRelation(provisionRelation,
                requirementRelation, isPlaceholderAssembly);

        model.add(provider);
        model.add(consumer);
        model.add(providerConsumerInterface);
        model.add(provisionRelation);
        model.add(requirementRelation);
        model.add(assemblyRelation);

        // Execution
        System system = transformer.transform(model);

        // Assertion
        assertNotNull(system);
        // TODO Check whether all elements where transformed correctly
    }

    @Override
    protected SystemTransformer createTransformer() {
        return new SystemTransformer();
    }

    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }
}
