package com.gstuer.modelmerging.framework.merger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.gstuer.modelmerging.framework.surrogate.Model;
import com.gstuer.modelmerging.framework.surrogate.Relation;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;

public abstract class RelationMergerTest<RM extends RelationMerger<M, R>, M extends Model, R extends Relation<T, S>,
        T extends Replaceable, S extends Replaceable> extends MergerTest<RM, M, R> {
    @Test
    public void testProcessAddsSourceAndDestinationToImplications() {
        // Test data
        M model = createEmptyModel();
        RM merger = createMerger(model);
        T source = getUniqueNonPlaceholderSourceEntity();
        S destination = getUniqueNonPlaceholderDestinationEntity();
        R relation = createRelation(source, destination, false);

        // Assertions Pre-Execution
        assertFalse(model.contains(source));
        assertFalse(model.contains(destination));
        assertFalse(model.contains(relation));
        assertFalse(merger.getImplications().contains(source));
        assertFalse(merger.getImplications().contains(destination));
        assertFalse(merger.getImplications().contains(relation));

        // Execution
        merger.process(relation);

        // Assertions Post-Execution
        assertFalse(model.contains(source));
        assertFalse(model.contains(destination));
        assertFalse(model.contains(relation));
        assertTrue(merger.getImplications().contains(source));
        assertTrue(merger.getImplications().contains(destination));
        assertFalse(merger.getImplications().contains(relation));
    }

    @Test
    public void testReplaceDirectPlaceholders() {
        // Test data
        M model = createEmptyModel();
        RM merger = createMerger(model);
        T source = getUniqueNonPlaceholderSourceEntity();
        S destination = getUniqueNonPlaceholderDestinationEntity();
        R relation = createRelation(source, destination, false);
        R placeholder = createRelation(source, destination, true);

        // Execution & Assertions to add placeholder
        merger.merge(placeholder);
        assertTrue(model.contains(placeholder));
        assertFalse(model.contains(relation));
        assertTrue(merger.getImplications().isEmpty());

        // Execution to replace placeholder
        merger.replaceDirectPlaceholders(relation);
        Set<Replaceable> implications = merger.getImplications();

        // Assertions
        assertFalse(model.contains(placeholder));
        assertFalse(model.contains(relation));
        assertFalse(implications.contains(placeholder));
        assertFalse(implications.contains(relation));
    }

    @Test
    public void testProcessReplacesDirectPlaceholder() {
        // Test data
        M model = createEmptyModel();
        RM merger = createMerger(model);
        T source = getUniqueNonPlaceholderSourceEntity();
        S destination = getUniqueNonPlaceholderDestinationEntity();
        R relation = createRelation(source, destination, false);
        R placeholder = createRelation(source, destination, true);

        // Execution & Assertions to add placeholder
        merger.merge(placeholder);
        assertTrue(model.contains(placeholder));
        assertFalse(model.contains(relation));
        assertTrue(merger.getImplications().isEmpty());

        // Execution to replace placeholder
        merger.process(relation);
        Set<Replaceable> implications = merger.getImplications();

        // Assertions
        assertFalse(model.contains(placeholder));
        assertTrue(model.contains(relation));
        assertFalse(implications.contains(placeholder));
        assertFalse(implications.contains(relation));
    }

    @Test
    public void testReplaceIndirectPlaceholdersSameSource() {
        // Test data
        M model = createEmptyModel();
        RM merger = createMerger(model);
        T source = getUniqueNonPlaceholderSourceEntity();
        S destination = getUniqueNonPlaceholderDestinationEntity();
        S destinationPlaceholder = getPlaceholderOfDestinationEntity(destination);
        R relation = createRelation(source, destination, true);
        R placeholder = createRelation(source, destinationPlaceholder, true);

        // Execution & Assertions to add placeholder
        merger.merge(placeholder);
        model.add(source);
        model.add(destinationPlaceholder);
        assertTrue(model.contains(placeholder));
        assertTrue(model.contains(source));
        assertTrue(model.contains(destinationPlaceholder));
        assertFalse(model.contains(destination));
        assertFalse(model.contains(relation));
        assertTrue(merger.getImplications().isEmpty());

        // Execution to replace placeholder
        merger.replaceIndirectPlaceholders(relation);
        Set<Replaceable> implications = merger.getImplications();

        // Assertions - Model State
        assertFalse(model.contains(placeholder));
        assertTrue(model.contains(source));
        assertFalse(model.contains(destinationPlaceholder));
        assertFalse(model.contains(destination));
        assertFalse(model.contains(relation));

        // Assertions - Implications
        assertFalse(implications.contains(placeholder));
        assertFalse(implications.contains(source));
        assertFalse(implications.contains(destinationPlaceholder));
        assertTrue(implications.contains(destination));
        assertFalse(implications.contains(relation));
    }

    @Test
    public void testReplaceIndirectPlaceholdersSameDestination() {
        // Test data
        M model = createEmptyModel();
        RM merger = createMerger(model);
        T source = getUniqueNonPlaceholderSourceEntity();
        T sourcePlaceholder = getPlaceholderOfSourceEntity(source);
        S destination = getUniqueNonPlaceholderDestinationEntity();
        R relation = createRelation(source, destination, true);
        R placeholder = createRelation(sourcePlaceholder, destination, true);

        // Execution & Assertions to add placeholder
        merger.merge(placeholder);
        model.add(sourcePlaceholder);
        model.add(destination);
        assertTrue(model.contains(placeholder));
        assertTrue(model.contains(sourcePlaceholder));
        assertTrue(model.contains(destination));
        assertFalse(model.contains(source));
        assertFalse(model.contains(relation));
        assertTrue(merger.getImplications().isEmpty());

        // Execution to replace placeholder
        merger.replaceIndirectPlaceholders(relation);
        Set<Replaceable> implications = merger.getImplications();

        // Assertions - Model State
        assertFalse(model.contains(placeholder));
        assertFalse(model.contains(sourcePlaceholder));
        assertTrue(model.contains(destination));
        assertFalse(model.contains(source));
        assertFalse(model.contains(relation));

        // Assertions - Implications
        assertFalse(implications.contains(placeholder));
        assertFalse(implications.contains(sourcePlaceholder));
        assertFalse(implications.contains(destination));
        assertTrue(implications.contains(source));
        assertFalse(implications.contains(relation));
    }

    @Test
    public void testProcessReplacesIndirectPlaceholder() {
        // Test data
        M model = createEmptyModel();
        RM merger = createMerger(model);
        T source = getUniqueNonPlaceholderSourceEntity();
        S destination = getUniqueNonPlaceholderDestinationEntity();
        S destinationPlaceholder = getPlaceholderOfDestinationEntity(destination);
        R relation = createRelation(source, destination, true);
        R placeholder = createRelation(source, destinationPlaceholder, true);

        // Execution & Assertions to add placeholder
        merger.merge(placeholder);
        model.add(source);
        model.add(destinationPlaceholder);
        assertTrue(model.contains(placeholder));
        assertTrue(model.contains(source));
        assertTrue(model.contains(destinationPlaceholder));
        assertFalse(model.contains(destination));
        assertFalse(model.contains(relation));
        assertTrue(merger.getImplications().isEmpty());

        // Execution to replace placeholder
        merger.process(relation);
        Set<Replaceable> implications = merger.getImplications();

        // Assertions - Model State
        assertFalse(model.contains(placeholder));
        assertTrue(model.contains(source));
        assertFalse(model.contains(destinationPlaceholder));
        assertTrue(model.contains(destination));
        assertTrue(model.contains(relation));

        // Assertions - Implications
        assertFalse(implications.contains(placeholder));
        assertFalse(implications.contains(source));
        assertFalse(implications.contains(destinationPlaceholder));
        assertTrue(implications.contains(destination));
        assertFalse(implications.contains(relation));
    }

    @Test
    public void testReplaceIndirectPlaceholdersNoReplaceSameSource() {
        // Test data
        M model = createEmptyModel();
        RM merger = createMerger(model);
        T source = getUniqueNonPlaceholderSourceEntity();
        S destination = getUniqueNonPlaceholderDestinationEntity();
        S otherDestination = getUniqueNonPlaceholderDestinationEntity();
        R relation = createRelation(source, destination, true);
        R otherRelation = createRelation(source, otherDestination, true);

        // Execution
        model.add(source);
        model.add(destination);
        model.add(otherDestination);
        model.add(otherRelation);
        merger.replaceIndirectPlaceholders(relation);
        Set<Replaceable> implications = merger.getImplications();

        // Assertions - Model State
        assertTrue(model.contains(source));
        assertTrue(model.contains(destination));
        assertTrue(model.contains(otherDestination));
        assertTrue(model.contains(otherRelation));
        assertFalse(model.contains(relation));

        // Assertions - Implications
        assertFalse(implications.contains(source));
        assertFalse(implications.contains(destination));
        assertFalse(implications.contains(otherDestination));
        assertFalse(implications.contains(otherRelation));
        assertFalse(implications.contains(relation));
    }

    @Test
    public void testReplaceIndirectPlaceholdersNoReplaceSameDestination() {
        // Test data
        M model = createEmptyModel();
        RM merger = createMerger(model);
        T source = getUniqueNonPlaceholderSourceEntity();
        T otherSource = getUniqueNonPlaceholderSourceEntity();
        S destination = getUniqueNonPlaceholderDestinationEntity();
        R relation = createRelation(source, destination, true);
        R otherRelation = createRelation(otherSource, destination, true);

        // Execution
        model.add(source);
        model.add(destination);
        model.add(otherSource);
        model.add(otherRelation);
        merger.replaceIndirectPlaceholders(relation);
        Set<Replaceable> implications = merger.getImplications();

        // Assertions - Model State
        assertTrue(model.contains(source));
        assertTrue(model.contains(destination));
        assertTrue(model.contains(otherSource));
        assertTrue(model.contains(otherRelation));
        assertFalse(model.contains(relation));

        // Assertions - Implications
        assertFalse(implications.contains(source));
        assertFalse(implications.contains(destination));
        assertFalse(implications.contains(otherSource));
        assertFalse(implications.contains(otherRelation));
        assertFalse(implications.contains(relation));
    }

    protected abstract R createRelation(T source, S destination, boolean isPlaceholder);

    protected abstract T getUniqueNonPlaceholderSourceEntity();

    protected abstract T getPlaceholderOfSourceEntity(T source);

    protected abstract S getUniqueNonPlaceholderDestinationEntity();

    protected abstract S getPlaceholderOfDestinationEntity(S destination);
}
