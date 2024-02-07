/* Copyright (C) 2023 Moritz Gstuer - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the EUPL 1.2 license.
 *
 * You should have received a copy of the EUPL 1.2 license
 * with this file. If not, please visit:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */
package tools.mdsd.mocore.framework.transformation;

import tools.mdsd.mocore.framework.surrogate.Model;

/**
 * A transformer acts as a bridging concept between the surrogate domain and another (arbitrary) domain. For this
 * purpose, a transformer consumes a {@link Model} instance and converts it into another model-knowledge-based object.
 *
 * @param <M> the consumed model type
 * @param <N> the provided model/object type
 */
public interface Transformer<M extends Model, N> {
    /**
     * Converts a surrogate {@link Model} instance into a model-knowledge-based object.
     *
     * @param model the {@link Model model} to be consumed
     * @return an object based on the content of the consumed {@link Model model}.
     */
    N transform(M model);
}
