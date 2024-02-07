/* Copyright (C) 2023 Moritz Gstuer - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the EUPL 1.2 license.
 *
 * You should have received a copy of the EUPL 1.2 license
 * with this file. If not, please visit:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */
package tools.mdsd.mocore.framework.surrogate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public abstract class ElementTest<T extends Element<S>, S> extends ReplaceableTest<T> {
    @Test
    public void testIncludesUnequalReplaceable() {
        // Test data
        T nonPlaceholder = getUniqueNonPlaceholder();
        T placeholder = getPlaceholderOf(getUniqueNonPlaceholder());

        // Assertions
        assertFalse(nonPlaceholder.includes(placeholder));
        assertFalse(nonPlaceholder.includes(getUniqueNonPlaceholder()));
        assertFalse(placeholder.includes(nonPlaceholder));
        assertFalse(placeholder.includes(getPlaceholderOf(getUniqueNonPlaceholder())));
    }

    @Test
    public void testGetValue() {
        // Test data
        S value = getUniqueValue();
        T nonPlaceholder = this.createElement(value, false);
        T placeholder = this.createElement(value, true);

        // Assertions
        assertEquals(value, nonPlaceholder.getValue());
        assertEquals(value, placeholder.getValue());
    }

    protected abstract T createElement(S value, boolean isPlaceholder);

    protected abstract S getUniqueValue();
}
