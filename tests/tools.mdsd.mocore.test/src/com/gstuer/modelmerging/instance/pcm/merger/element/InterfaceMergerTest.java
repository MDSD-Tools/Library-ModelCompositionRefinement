package com.gstuer.modelmerging.instance.pcm.merger.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.gstuer.modelmerging.framework.merger.MergerTest;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Interface;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceProvisionRelation;

public class InterfaceMergerTest extends MergerTest<InterfaceMerger, PcmSurrogate, Interface> {
    @Test
    public void testRefineWithValidElementAddsCorrectImplications() {
        // Test data
        PcmSurrogate model = createEmptyModel();
        InterfaceMerger merger = createMerger(model);
        Interface element = createUniqueReplaceable();

        // Assertions: Pre-execution
        assertTrue(merger.getImplications().isEmpty());

        // Execution
        merger.refine(element);
        Set<Replaceable> implications = merger.getImplications();

        // Assertions: Post-execution
        assertEquals(1, implications.size());
        Replaceable implication = implications.stream().findFirst().orElseThrow();
        assertEquals(InterfaceProvisionRelation.class, implication.getClass());
        InterfaceProvisionRelation relation = (InterfaceProvisionRelation) implication;
        assertEquals(element, relation.getDestination());
        assertTrue(relation.isPlaceholder());
        assertTrue(relation.getSource().isPlaceholder());
    }

    @Override
    protected InterfaceMerger createMerger(PcmSurrogate model) {
        return new InterfaceMerger(model);
    }

    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }

    @Override
    protected Interface createUniqueReplaceable() {
        return Interface.getUniquePlaceholder();
    }
}
