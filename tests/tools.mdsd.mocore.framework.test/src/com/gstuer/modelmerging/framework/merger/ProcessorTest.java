package com.gstuer.modelmerging.framework.merger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;

import com.gstuer.modelmerging.framework.surrogate.Model;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;

public abstract class ProcessorTest<U extends Processor<M, T>, M extends Model, T extends Replaceable> {
    protected static final String TEST_API_ONLY_METHOD_NAME = "testApiOnly";
    private static final boolean TEST_API_ONLY = false;

    @Test
    public void testGetModelAfterCreation() {
        // Test data
        M model = createEmptyModel();
        U processor = createProcessor(model);

        // Assertions
        assertNotNull(processor.getModel());
        assertEquals(model, processor.getModel());
    }

    @Test
    public void testGetImplicationsAfterCreation() {
        // Test data
        M model = createEmptyModel();
        U processor = createProcessor(model);

        // Execution
        Set<Replaceable> implications = processor.getImplications();

        // Assertions
        assertNotNull(implications);
        assertTrue(implications.isEmpty());
    }

    @Test
    public void testGetProcessableType() {
        // Test data
        M model = createEmptyModel();
        U processor = createProcessor(model);
        T replaceable = createUniqueReplaceable();

        // Assertions
        assertEquals(replaceable.getClass(), processor.getProcessableType());
    }

    @Test
    @DisabledIf(TEST_API_ONLY_METHOD_NAME)
    public void testMergeAddsReplaceableToModel() {
        // Test data
        M model = createEmptyModel();
        U processor = createProcessor(model);
        T replaceable = createUniqueReplaceable();

        // Assertions Pre-Execution
        assertFalse(model.contains(replaceable));

        // Execution
        processor.merge(replaceable);

        // Assertions Post-Execution
        assertTrue(model.contains(replaceable));
    }

    @Test
    public void testProcessExecutesMerge() {
        // Test data
        M model = createEmptyModel();
        U processor = createProcessor(model);
        T replaceable = createUniqueReplaceable();

        // Assertions Pre-Execution
        assertFalse(model.contains(replaceable));

        // Execution
        processor.process(replaceable);

        // Assertions Post-Execution
        assertTrue(model.contains(replaceable));
    }

    @Test
    @DisabledIf(TEST_API_ONLY_METHOD_NAME)
    public void testProcessClearsImplications() {
        // Test data
        M model = createEmptyModel();
        U processor = createProcessor(model);
        T replaceable = createUniqueReplaceable();

        // Assertions Pre-Execution
        processor.addImplication(replaceable);
        assertTrue(processor.getImplications().contains(replaceable));
        assertFalse(processor.getImplications().isEmpty());

        // Execution
        processor.process(replaceable);
        Set<Replaceable> implications = processor.getImplications();

        // Assertions Post-Execution
        assertFalse(implications.contains(replaceable));
    }

    protected abstract U createProcessor(M model);

    protected abstract M createEmptyModel();

    protected abstract T createUniqueReplaceable();

    protected static boolean testApiOnly() {
        return TEST_API_ONLY;
    }
}
