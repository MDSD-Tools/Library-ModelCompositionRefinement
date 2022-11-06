package com.gstuer.modelmerging.instance.pcm.merger.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;

import com.gstuer.modelmerging.framework.merger.ProcessorTest;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.LinkResourceSpecification;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.LinkResourceSpecificationRelation;

public class LinkResourceSpecificationMergerTest
        extends ProcessorTest<LinkResourceSpecificationMerger, PcmSurrogate, LinkResourceSpecification> {
    @Test
    @DisabledIf(TEST_API_ONLY_METHOD_NAME)
    public void testRefineWithValidElementAddsCorrectImplications() {
        // Test data
        PcmSurrogate model = createEmptyModel();
        LinkResourceSpecificationMerger merger = createProcessor(model);
        LinkResourceSpecification element = createUniqueReplaceable();

        // Assertions: Pre-execution
        assertTrue(merger.getImplications().isEmpty());

        // Execution
        merger.refine(element);
        Set<Replaceable> implications = merger.getImplications();

        // Assertions: Post-execution
        assertEquals(1, implications.size());
        Replaceable implication = implications.stream().findFirst().orElseThrow();
        assertEquals(LinkResourceSpecificationRelation.class, implication.getClass());
        LinkResourceSpecificationRelation relation = (LinkResourceSpecificationRelation) implication;
        assertEquals(element, relation.getSource());
        assertTrue(relation.isPlaceholder());
        assertTrue(relation.getDestination().isPlaceholder());
        assertTrue(relation.getDestination().getSource().isPlaceholder());
        assertTrue(relation.getDestination().getDestination().isPlaceholder());
    }

    @Override
    protected LinkResourceSpecificationMerger createProcessor(PcmSurrogate model) {
        return new LinkResourceSpecificationMerger(model);
    }

    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }

    @Override
    protected LinkResourceSpecification createUniqueReplaceable() {
        return LinkResourceSpecification.getUniquePlaceholder();
    }
}
