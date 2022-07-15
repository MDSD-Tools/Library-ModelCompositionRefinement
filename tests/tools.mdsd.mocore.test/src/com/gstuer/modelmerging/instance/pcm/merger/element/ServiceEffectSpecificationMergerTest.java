package com.gstuer.modelmerging.instance.pcm.merger.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.gstuer.modelmerging.framework.merger.MergerTest;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.ServiceEffectSpecification;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.SignatureSpecificationRelation;

public class ServiceEffectSpecificationMergerTest
        extends MergerTest<ServiceEffectSpecificationMerger, PcmSurrogate, ServiceEffectSpecification> {
    @Test
    public void testRefineWithValidElementAddsCorrectImplications() {
        // Test data
        PcmSurrogate model = createEmptyModel();
        ServiceEffectSpecificationMerger merger = createMerger(model);
        ServiceEffectSpecification element = createUniqueReplaceable();

        // Assertions: Pre-execution
        assertTrue(merger.getImplications().isEmpty());

        // Execution
        merger.refine(element);
        Set<Replaceable> implications = merger.getImplications();

        // Assertions: Post-execution
        assertEquals(1, implications.size());
        Replaceable implication = implications.stream().findFirst().orElseThrow();
        assertEquals(SignatureSpecificationRelation.class, implication.getClass());
        SignatureSpecificationRelation relation = (SignatureSpecificationRelation) implication;
        assertEquals(element, relation.getDestination());
        assertTrue(relation.isPlaceholder());
        assertTrue(relation.getSource().isPlaceholder());
    }

    @Override
    protected ServiceEffectSpecificationMerger createMerger(PcmSurrogate model) {
        return new ServiceEffectSpecificationMerger(model);
    }

    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }

    @Override
    protected ServiceEffectSpecification createUniqueReplaceable() {
        return ServiceEffectSpecification.getUniquePlaceholder();
    }

}
