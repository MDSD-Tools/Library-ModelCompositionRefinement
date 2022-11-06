package com.gstuer.modelmerging.instance.pcm.processor.element;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;

import com.gstuer.modelmerging.framework.processor.ProcessorTest;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;

public class DeploymentProcessorTest extends ProcessorTest<DeploymentProcessor, PcmSurrogate, Deployment> {
    @Test
    @DisabledIf(TEST_API_ONLY_METHOD_NAME)
    public void testRefineWithValidElementAddsCorrectImplications() {
        // Test data
        PcmSurrogate model = createEmptyModel();
        DeploymentProcessor merger = createProcessor(model);
        Deployment element = createUniqueReplaceable();

        // Assertions: Pre-execution
        assertTrue(merger.getImplications().isEmpty());

        // Execution
        merger.refine(element);

        // Assertions: Post-execution
        assertTrue(merger.getImplications().isEmpty());
    }

    @Override
    protected DeploymentProcessor createProcessor(PcmSurrogate model) {
        return new DeploymentProcessor(model);
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
