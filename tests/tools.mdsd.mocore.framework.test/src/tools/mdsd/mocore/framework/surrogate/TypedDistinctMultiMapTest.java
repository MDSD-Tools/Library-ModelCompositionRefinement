/* Copyright (C) 2023 Moritz Gstuer - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the EUPL 1.2 license.
 *
 * You should have received a copy of the EUPL 1.2 license
 * with this file. If not, please visit:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */
package tools.mdsd.mocore.framework.surrogate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TypedDistinctMultiMapTest {
    @Test
    public void testPutValidElements() {
        // Test data
        TypedDistinctMultiMap<Object> map = new TypedDistinctMultiMap<>();
        String entityFst = "Test";
        String entitySnd = "Entity";

        // Execution
        map.put(entityFst);
        map.put(entitySnd);
        map.put(1L);
        map.put(2f);

        // Assertions
        assertTrue(map.containsElement(entityFst));
        assertTrue(map.containsElement(entitySnd));
        assertTrue(map.containsElement(1L));
        assertTrue(map.containsElement(2f));
    }

    @Test
    public void testPutAndGetDuplicates() {
        // Test data
        TypedDistinctMultiMap<Object> map = new TypedDistinctMultiMap<>();
        String entity = "Test";

        // Execution
        map.put(entity);
        map.put(entity);
        map.put(entity);
        List<String> elements = map.get(String.class);

        // Assertions
        assertTrue(map.containsElement(entity));
        assertEquals(1, elements.size());
    }

    @Test
    public void testPutNull() {
        // Test data
        TypedDistinctMultiMap<Object> map = new TypedDistinctMultiMap<>();

        // Assertions: Post-Execution
        assertThrows(NullPointerException.class, () -> map.put(null));
    }

    @Test
    public void testGetExistingKey() {
        // Test data
        TypedDistinctMultiMap<Object> map = new TypedDistinctMultiMap<>();
        String entityFst = "Test";
        String entitySnd = "Entity";
        map.put(entityFst);
        map.put(entitySnd);

        // Execution
        List<String> elements = map.get(String.class);

        // Assertions: Post-Execution
        assertEquals(2, elements.size());
        assertTrue(elements.contains(entityFst));
        assertTrue(elements.contains(entitySnd));
    }

    @Test
    public void testGetUnkownKey() {
        // Test data
        TypedDistinctMultiMap<Object> map = new TypedDistinctMultiMap<>();
        String entityFst = "Test";
        String entitySnd = "Entity";
        map.put(entityFst);
        map.put(entitySnd);

        // Execution
        List<Integer> elements = map.get(Integer.class);

        // Assertions: Post-Execution
        assertEquals(0, elements.size());
    }

    @Test
    public void testGetChildKeys() {
        // Test data
        TypedDistinctMultiMap<Object> map = new TypedDistinctMultiMap<>();
        String entityFst = "Test";
        String entitySnd = "Entity";
        map.put(entityFst);
        map.put(entitySnd);

        // Execution
        List<Object> elements = map.get(Object.class, false);

        // Assertions: Post-Execution
        assertEquals(2, elements.size());
    }

    @Test
    public void testGetIgnoreChildKeys() {
        // Test data
        TypedDistinctMultiMap<Object> map = new TypedDistinctMultiMap<>();
        String entityFst = "Test";
        String entitySnd = "Entity";
        map.put(entityFst);
        map.put(entitySnd);

        // Execution
        List<Object> elements = map.get(Object.class, true);

        // Assertions: Post-Execution
        assertEquals(0, elements.size());
    }

    @Test
    public void testGetNull() {
        // Test data
        TypedDistinctMultiMap<Object> map = new TypedDistinctMultiMap<>();

        // Assertions: Post-Execution
        assertThrows(NullPointerException.class, () -> map.get(null));
    }

    @Test
    public void testRemoveExistingElement() {
        // Test data
        TypedDistinctMultiMap<Object> map = new TypedDistinctMultiMap<>();
        String entity = "Test";
        map.put(entity);

        // Assertions: Pre-Execution
        assertTrue(map.containsElement(entity));

        // Execution
        map.remove(entity);

        // Assertions: Post-Execution
        assertFalse(map.containsElement(entity));
    }

    @Test
    public void testRemoveUnkownElement() {
        // Test data
        TypedDistinctMultiMap<Object> map = new TypedDistinctMultiMap<>();
        String entity = "Test";
        map.put(entity);

        // Assertions: Pre-Execution
        assertTrue(map.containsElement(entity));

        // Execution
        assertDoesNotThrow(() -> map.remove(new Object()));
        assertDoesNotThrow(() -> map.remove(String.class));

        // Assertions: Post-Execution
        assertTrue(map.containsElement(entity));
    }

    @Test
    public void testRemoveNull() {
        // Test data
        TypedDistinctMultiMap<Object> map = new TypedDistinctMultiMap<>();

        // Assertions: Post-Execution
        assertThrows(NullPointerException.class, () -> map.remove(null));
    }

    @Test
    public void testContainsKeyValidKey() {
        // Test data
        TypedDistinctMultiMap<Object> map = new TypedDistinctMultiMap<>();
        String entity = "Test";

        // Execution
        map.put(entity);

        // Assertions
        assertTrue(map.containsKey(String.class));
    }

    @Test
    public void testContainsKeyUnknownKey() {
        // Test data
        TypedDistinctMultiMap<Object> map = new TypedDistinctMultiMap<>();
        String entity = "Test";

        // Execution
        map.put(entity);

        // Assertions
        assertFalse(map.containsKey(Object.class));
        assertFalse(map.containsKey(Integer.class));
    }

    @Test
    public void testContainsKeyNull() {
        // Test data
        TypedDistinctMultiMap<Object> map = new TypedDistinctMultiMap<>();

        // Assertions
        assertFalse(map.containsKey(null));
    }

    @Test
    public void testContainsElementValidElement() {
        // Test data
        TypedDistinctMultiMap<Object> map = new TypedDistinctMultiMap<>();
        String entity = "Test";

        // Execution
        map.put(entity);

        // Assertions
        assertTrue(map.containsElement(entity));
    }

    @Test
    public void testContainsElementUnknownElement() {
        // Test data
        TypedDistinctMultiMap<Object> map = new TypedDistinctMultiMap<>();
        String entity = "Test";

        // Execution
        map.put(entity);

        // Assertions
        assertFalse(map.containsElement(new Object()));
        assertFalse(map.containsElement(String.class));
        assertFalse(map.containsElement(1L));
    }

    @Test
    public void testContainsElementNull() {
        // Test data
        TypedDistinctMultiMap<Object> map = new TypedDistinctMultiMap<>();

        // Assertions
        assertFalse(map.containsElement(null));
    }

    @Test
    public void testIteratorNotEmpty() {
        // Test data
        TypedDistinctMultiMap<Object> map = new TypedDistinctMultiMap<>();
        String entity = "Test";

        // Execution
        map.put(entity);
        map.put(1L);
        map.put(2f);
        Iterator<Object> iterator = map.iterator();

        // Assertions
        assertTrue(iterator.hasNext());
        int elementCount = 0;
        while (iterator.hasNext()) {
            Object element = iterator.next();
            if (element.getClass() == String.class) {
                assertEquals(entity, element);
                elementCount++;
            } else if (element.getClass() == Long.class) {
                assertEquals(1L, element);
                elementCount++;
            } else if (element.getClass() == Float.class) {
                assertEquals(2f, element);
                elementCount++;
            } else {
                fail();
            }
        }
        assertEquals(3, elementCount);
    }

    @Test
    public void testIteratorEmpty() {
        // Test data
        TypedDistinctMultiMap<Object> map = new TypedDistinctMultiMap<>();

        // Execution
        Iterator<Object> iterator = map.iterator();

        // Assertions
        assertFalse(iterator.hasNext());
    }
}
