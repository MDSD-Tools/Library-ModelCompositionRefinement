package tools.mdsd.mocore.framework.surrogate;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

public abstract class ModelTest<T extends Model, S extends Replaceable> {
    @Test
    public void testAddAndContainsWithValidReplaceable() {
        // Test data
        T model = createEmptyModel();
        S replaceable = createUniqueReplaceable();

        // Assertions PreExecution
        assertFalse(model.contains(replaceable));

        // Execution
        model.add(replaceable);

        // Assertions PostExecution
        assertTrue(model.contains(replaceable));
    }

    @Test
    public void testAddDuplicates() {
        // Test data
        T model = createEmptyModel();
        S replaceable = createUniqueReplaceable();

        // Assertions PreExecution
        List<?> replaceables = model.getByType(replaceable.getClass());
        assertEquals(0, replaceables.size());

        // Execution
        model.add(replaceable);
        model.add(replaceable);
        model.add(replaceable);

        // Assertions PostExecution
        replaceables = model.getByType(replaceable.getClass());
        assertEquals(1, replaceables.size());
    }

    @Test
    public void testAddNull() {
        // Test data
        T model = createEmptyModel();

        // Assertions
        assertThrows(RuntimeException.class, () -> model.add(null));
    }

    @Test
    public void testContainsNull() {
        // Test data
        T model = createEmptyModel();

        // Assertions
        assertFalse(model.contains(null));
    }

    @Test
    public void testGetByTypeWithValidReplaceables() {
        // Test data
        T model = createEmptyModel();
        S replaceableFst = createUniqueReplaceable();
        S replaceableSnd = createUniqueReplaceable();
        S replaceableTrd = createUniqueReplaceable();

        // Execution
        model.add(replaceableFst);
        model.add(replaceableSnd);
        model.add(replaceableTrd);

        // Assertions
        List<?> replaceables = model.getByType(replaceableFst.getClass());
        assertEquals(3, replaceables.size());
        assertTrue(replaceables.contains(replaceableFst));
        assertTrue(replaceables.contains(replaceableSnd));
        assertTrue(replaceables.contains(replaceableTrd));
    }

    @Test
    public void testReplaceEqualEntities() {
        // Test data
        T model = createEmptyModel();
        S replaceable = createUniqueReplaceable();
        model.add(replaceable);
        model.add(createUniqueReplaceable());

        // Assertions PreExecution
        assertTrue(model.contains(replaceable));
        assertEquals(2, model.getByType(replaceable.getClass()).size());

        // Execution
        Set<Replaceable> implications = model.replace(replaceable, replaceable);

        // Assertions PostExecution
        assertFalse(model.contains(replaceable));
        assertEquals(1, model.getByType(replaceable.getClass()).size());
        assertTrue(implications.contains(replaceable));
        assertEquals(1, implications.size());
    }

    @Test
    public void testReplaceUnknownOriginal() {
        // Test data
        T model = createEmptyModel();
        S replaceable = createUniqueReplaceable();
        model.add(replaceable);

        // Assertions PreExecution
        assertTrue(model.contains(replaceable));
        assertEquals(1, model.getByType(replaceable.getClass()).size());

        // Execution
        Set<Replaceable> implications = model.replace(createUniqueReplaceable(), replaceable);

        // Assertions PostExecution
        assertTrue(model.contains(replaceable));
        assertEquals(1, model.getByType(replaceable.getClass()).size());
        assertFalse(implications.contains(replaceable));
        assertEquals(0, implications.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReplaceElementWithinRelation() {
        // Test data
        T model = createEmptyModel();
        S source = createUniqueReplaceable();
        S destination = createUniqueReplaceable();
        S replacementDestination = createUniqueReplaceable();
        Relation<S, S> relation = createRelation(source, destination, false);

        // Assertions PreExecution
        model.add(source);
        model.add(destination);
        model.add(relation);
        assertTrue(model.contains(source));
        assertTrue(model.contains(destination));
        assertFalse(model.contains(replacementDestination));
        assertTrue(model.contains(relation));

        // Execution
        Set<Replaceable> implications = model.replace(destination, replacementDestination);

        // Assertions PostExecution
        assertTrue(model.contains(source));
        assertFalse(model.contains(destination));
        assertFalse(model.contains(replacementDestination));
        assertFalse(model.contains(relation));

        assertEquals(2, implications.size());
        assertTrue(implications.contains(replacementDestination));
        assertFalse(implications.contains(relation));
        implications.remove(replacementDestination);
        implications.forEach(implication -> {
            assertEquals(relation.getClass(), implication.getClass());
            assertEquals(source, ((Relation<S, S>) implication).getSource());
            assertEquals(replacementDestination, ((Relation<S, S>) implication).getDestination());
            assertEquals(relation.isPlaceholder(), implication.isPlaceholder());
        });
    }

    @Test
    public void testReplacePlaceholderRelation() {
        // Test data
        T model = createEmptyModel();
        S source = createUniqueReplaceable();
        S destination = createUniqueReplaceable();
        Relation<S, S> placeholder = createRelation(source, destination, true);
        Relation<S, S> nonPlaceholder = createRelation(source, destination, false);

        // Assertions PreExecution
        model.add(source);
        model.add(destination);
        model.add(placeholder);
        assertTrue(model.contains(source));
        assertTrue(model.contains(destination));
        assertTrue(model.contains(placeholder));
        assertFalse(model.contains(nonPlaceholder));

        // Execution
        Set<Replaceable> implications = model.replace(placeholder, nonPlaceholder);

        // Assertions PostExecution
        assertTrue(model.contains(source));
        assertTrue(model.contains(destination));
        assertFalse(model.contains(placeholder));
        assertFalse(model.contains(nonPlaceholder));

        assertEquals(1, implications.size());
        assertTrue(implications.contains(nonPlaceholder));
    }

    @Test
    public void testReplaceNullOriginalOrReplacement() {
        // Test data
        T model = createEmptyModel();
        S replaceable = createUniqueReplaceable();

        // Execution
        model.add(replaceable);

        // Assertions
        assertThrows(RuntimeException.class, () -> model.replace(null, replaceable));
        assertThrows(RuntimeException.class, () -> model.replace(replaceable, null));
        assertThrows(RuntimeException.class, () -> model.replace(null, null));
    }

    protected abstract T createEmptyModel();

    protected abstract S createUniqueReplaceable();

    protected abstract Relation<S, S> createRelation(S source, S destination, boolean isPlaceholder);
}
