package com.gstuer.modelmerging.instance.pcm.merger.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.gstuer.modelmerging.framework.merger.MergerTest;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAllocationRelation;

public class ComponentMergerTest extends MergerTest<ComponentMerger, PcmSurrogate, Component> {
    @Test
    public void testRefineWithValidElementAddsCorrectImplications() {
        // Test data
        PcmSurrogate model = createEmptyModel();
        ComponentMerger merger = createMerger(model);
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
        assertTrue(relation.getDestination().isPlaceholder());
    }

    @Override
    protected ComponentMerger createMerger(PcmSurrogate model) {
        return new ComponentMerger(model);
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
