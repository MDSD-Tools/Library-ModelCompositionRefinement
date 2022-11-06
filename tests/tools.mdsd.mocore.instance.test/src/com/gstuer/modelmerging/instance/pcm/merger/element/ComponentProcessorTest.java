package com.gstuer.modelmerging.instance.pcm.merger.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;

import com.gstuer.modelmerging.framework.merger.ProcessorTest;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAllocationRelation;

public class ComponentProcessorTest extends ProcessorTest<ComponentProcessor, PcmSurrogate, Component> {
    @Test
    @DisabledIf(TEST_API_ONLY_METHOD_NAME)
    public void testRefineWithValidElementAddsCorrectImplications() {
        // Test data
        PcmSurrogate model = createEmptyModel();
        ComponentProcessor merger = createProcessor(model);
        Component element = createUniqueReplaceable();

        // Assertions: Pre-execution
        assertTrue(merger.getImplications().isEmpty());

        // Execution
        merger.refine(element);
        Set<Replaceable> implications = merger.getImplications();

        // Assertions: Post-execution
        assertEquals(1, implications.size());
        Replaceable implication = implications.stream().findFirst().orElseThrow();
        assertEquals(ComponentAllocationRelation.class, implication.getClass());
        ComponentAllocationRelation relation = (ComponentAllocationRelation) implication;
        assertEquals(element, relation.getSource());
        assertTrue(relation.isPlaceholder());
        assertTrue(relation.getDestination().isPlaceholder());
    }

    @Override
    protected ComponentProcessor createProcessor(PcmSurrogate model) {
        return new ComponentProcessor(model);
    }

    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }

    @Override
    protected Component createUniqueReplaceable() {
        return Component.getUniquePlaceholder();
    }
}
