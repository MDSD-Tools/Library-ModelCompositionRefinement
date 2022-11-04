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

public abstract class MergerTest<U extends Merger<M, T>, M extends Model, T extends Replaceable> {
    protected static final String TEST_API_ONLY_METHOD_NAME = "testApiOnly";
    private static final boolean TEST_API_ONLY = true;

    @Test
    public void testGetModelAfterCreation() {
        // Test data
        M model = createEmptyModel();
        U merger = createMerger(model);

        // Assertions
        assertNotNull(merger.getModel());
        assertEquals(model, merger.getModel());
    }

    @Test
    public void testGetImplicationsAfterCreation() {
        // Test data
        M model = createEmptyModel();
        U merger = createMerger(model);

        // Execution
        Set<Replaceable> implications = merger.getImplications();

        // Assertions
        assertNotNull(implications);
        assertTrue(implications.isEmpty());
    }

    @Test
    public void testGetProcessableType() {
        // Test data
        M model = createEmptyModel();
        U merger = createMerger(model);
        T replaceable = createUniqueReplaceable();

        // Assertions
        assertEquals(replaceable.getClass(), merger.getProcessableType());
    }

    @Test
    @DisabledIf(TEST_API_ONLY_METHOD_NAME)
    public void testMergeAddsReplaceableToModel() {
        // Test data
        M model = createEmptyModel();
        U merger = createMerger(model);
        T replaceable = createUniqueReplaceable();

        // Assertions Pre-Execution
        assertFalse(model.contains(replaceable));

        // Execution
        merger.merge(replaceable);

        // Assertions Post-Execution
        assertTrue(model.contains(replaceable));
    }

    @Test
    public void testProcessExecutesMerge() {
        // Test data
        M model = createEmptyModel();
        U merger = createMerger(model);
        T replaceable = createUniqueReplaceable();

        // Assertions Pre-Execution
        assertFalse(model.contains(replaceable));

        // Execution
        merger.process(replaceable);

        // Assertions Post-Execution
        assertTrue(model.contains(replaceable));
    }

    @Test
    @DisabledIf(TEST_API_ONLY_METHOD_NAME)
    public void testProcessClearsImplications() {
        // Test data
        M model = createEmptyModel();
        U merger = createMerger(model);
        T replaceable = createUniqueReplaceable();

        // Assertions Pre-Execution
        merger.addImplication(replaceable);
        assertTrue(merger.getImplications().contains(replaceable));
        assertFalse(merger.getImplications().isEmpty());

        // Execution
        merger.process(replaceable);
        Set<Replaceable> implications = merger.getImplications();

        // Assertions Post-Execution
        assertFalse(implications.contains(replaceable));
    }

    protected abstract U createMerger(M model);

    protected abstract M createEmptyModel();

    protected abstract T createUniqueReplaceable();

    protected static boolean testApiOnly() {
        return TEST_API_ONLY;
    }
}
