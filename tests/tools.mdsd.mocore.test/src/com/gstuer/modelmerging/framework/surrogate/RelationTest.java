package com.gstuer.modelmerging.framework.surrogate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public abstract class RelationTest<R extends Relation<T, S>, T extends Replaceable, S extends Replaceable>
        extends ReplaceableTest<R> {
    @Test
    public void testGetSource() {
        // Test data
        T source = getUniqueSourceEntity();
        S destination = getUniqueDestinationEntity();
        R nonPlaceholder = this.createRelation(source, destination, false);
        R placeholder = this.createRelation(source, destination, true);

        // Assertions
        assertEquals(source, nonPlaceholder.getSource());
        assertEquals(source, placeholder.getSource());
    }

    @Test
    public void testGetDestination() {
        // Test data
        T source = getUniqueSourceEntity();
        S destination = getUniqueDestinationEntity();
        R nonPlaceholder = this.createRelation(source, destination, false);
        R placeholder = this.createRelation(source, destination, true);

        // Assertions
        assertEquals(destination, nonPlaceholder.getDestination());
        assertEquals(destination, placeholder.getDestination());
    }

    @Test
    public void testCanReplaceEqualSource() {
        // Test data
        T source = getUniqueSourceEntity();
        S destination = getUniqueDestinationEntity();
        R nonPlaceholder = this.createRelation(source, destination, false);
        R placeholder = this.createRelation(source, destination, true);

        // Assertions
        assertTrue(nonPlaceholder.canReplace(source));
        assertTrue(placeholder.canReplace(source));
    }

    @Test
    public void testCanReplaceEqualDestination() {
        // Test data
        T source = getUniqueSourceEntity();
        S destination = getUniqueDestinationEntity();
        R nonPlaceholder = this.createRelation(source, destination, false);
        R placeholder = this.createRelation(source, destination, true);

        // Assertions
        assertTrue(nonPlaceholder.canReplace(destination));
        assertTrue(placeholder.canReplace(destination));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReplaceSource() {
        // Test data
        T originalSource = getUniqueSourceEntity();
        T replacementSource = getUniqueSourceEntity();
        S destination = getUniqueDestinationEntity();
        R relation = this.createRelation(originalSource, destination, false);

        // Execution -> Implicitly asserts correct return type of replace()
        R replaceRelation = (R) relation.replace(originalSource, replacementSource);

        // Assertions
        assertEquals(replacementSource, replaceRelation.getSource());
        assertEquals(destination, replaceRelation.getDestination());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReplaceDestination() {
        // Test data
        T source = getUniqueSourceEntity();
        S originalDestination = getUniqueDestinationEntity();
        S replacementDestination = getUniqueDestinationEntity();
        R relation = this.createRelation(source, originalDestination, false);

        // Execution -> Implicitly asserts correct return type of replace()
        R replaceRelation = (R) relation.replace(originalDestination, replacementDestination);

        // Assertions
        assertEquals(source, replaceRelation.getSource());
        assertEquals(replacementDestination, replaceRelation.getDestination());
    }

    @Override
    protected R getUniqueNonPlaceholder() {
        return createRelation(getUniqueSourceEntity(), getUniqueDestinationEntity(), false);
    }

    @Override
    protected R getPlaceholderOf(R replaceable) {
        T source = replaceable.getSource();
        S destination = replaceable.getDestination();
        return createRelation(source, destination, true);
    }

    protected abstract R createRelation(T source, S destination, boolean isPlaceholder);

    protected abstract T getUniqueSourceEntity();

    protected abstract S getUniqueDestinationEntity();
}
