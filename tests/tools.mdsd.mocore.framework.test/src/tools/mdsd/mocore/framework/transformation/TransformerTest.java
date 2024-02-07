/* Copyright (C) 2023 Moritz Gstuer - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the EUPL 1.2 license.
 *
 * You should have received a copy of the EUPL 1.2 license
 * with this file. If not, please visit:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */
package tools.mdsd.mocore.framework.transformation;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;

import tools.mdsd.mocore.framework.surrogate.Model;

public abstract class TransformerTest<T extends Transformer<M, N>, M extends Model, N> {
    @Test
    public void testTransformEmptyModel() {
        // Test data
        T transformer = createTransformer();
        M model = createEmptyModel();

        // Execution
        N result = transformer.transform(model);

        // Assertions
        assertNotNull(result);
    }

    protected abstract T createTransformer();

    protected abstract M createEmptyModel();
}
