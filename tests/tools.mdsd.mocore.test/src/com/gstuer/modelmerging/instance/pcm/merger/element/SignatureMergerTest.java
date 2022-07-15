package com.gstuer.modelmerging.instance.pcm.merger.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.gstuer.modelmerging.framework.merger.MergerTest;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Signature;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.SignatureProvisionRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.SignatureSpecificationRelation;

public class SignatureMergerTest extends MergerTest<SignatureMerger, PcmSurrogate, Signature> {
    @Test
    public void testRefineWithValidElementAddsCorrectImplications() {
        // Test data
        PcmSurrogate model = createEmptyModel();
        SignatureMerger merger = createMerger(model);
        Signature element = createUniqueReplaceable();

        // Assertions: Pre-execution
        assertTrue(merger.getImplications().isEmpty());

        // Execution
        merger.refine(element);
        Set<Replaceable> implications = new HashSet<>(merger.getImplications());

        // Assertions: Post-execution
        //// Implicit effect specification
        assertEquals(2, implications.size());
        Replaceable implication = implications.stream()
                .filter(replacable -> replacable.getClass().equals(SignatureSpecificationRelation.class))
                .findFirst().orElseThrow();
        SignatureSpecificationRelation implicitSpecification = (SignatureSpecificationRelation) implication;
        assertEquals(element, implicitSpecification.getSource());
        assertTrue(implicitSpecification.isPlaceholder());
        assertTrue(implicitSpecification.getDestination().isPlaceholder());
        assertTrue(implications.remove(implicitSpecification));

        //// Implicit providing interface
        assertEquals(1, implications.size());
        implication = implications.stream().findFirst().orElseThrow();
        assertEquals(SignatureProvisionRelation.class, implication.getClass());
        SignatureProvisionRelation relation = (SignatureProvisionRelation) implication;
        assertEquals(element, relation.getSource());
        assertTrue(relation.isPlaceholder());
        assertTrue(relation.getDestination().isPlaceholder());
    }

    @Override
    protected SignatureMerger createMerger(PcmSurrogate model) {
        return new SignatureMerger(model);
    }

    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }

    @Override
    protected Signature createUniqueReplaceable() {
        return Signature.getUniquePlaceholder();
    }
}
