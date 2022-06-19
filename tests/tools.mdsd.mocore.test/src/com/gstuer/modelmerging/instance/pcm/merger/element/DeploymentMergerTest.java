package com.gstuer.modelmerging.instance.pcm.merger.element;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import com.gstuer.modelmerging.framework.merger.MergerTest;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;

public class DeploymentMergerTest extends MergerTest<DeploymentMerger, PcmSurrogate, Deployment> {
    @Test
    public void testRefineWithValidElementAddsCorrectImplications() {
        // Test data
        PcmSurrogate model = createEmptyModel();
        DeploymentMerger merger = createMerger(model);
        Deployment element = createUniqueReplaceable();

        // Assertions: Pre-execution
        assertTrue(merger.getImplications().isEmpty());

        // Execution
        merger.refine(element);

        // Assertions: Post-execution
        assertTrue(merger.getImplications().isEmpty());
    }

    @Override
    protected DeploymentMerger createMerger(PcmSurrogate model) {
        return new DeploymentMerger(model);
    }

    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }

    @Override
    protected Deployment createUniqueReplaceable() {
        return Deployment.getUniquePlaceholder();
    }
}
