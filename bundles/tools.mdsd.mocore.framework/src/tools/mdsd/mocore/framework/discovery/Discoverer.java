/* Copyright (C) 2023 Moritz Gstuer - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the EUPL 1.2 license.
 *
 * You should have received a copy of the EUPL 1.2 license
 * with this file. If not, please visit:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */
package tools.mdsd.mocore.framework.discovery;

import java.util.Objects;
import java.util.Set;

import tools.mdsd.mocore.framework.surrogate.Replaceable;

/**
 * Represents a provider for a specific type of {@link Replaceable} instances.
 *
 * @param <T> the type of {@link Replaceable} the discoverer provides
 */
public abstract class Discoverer<T extends Replaceable> {
    private final Set<T> discoveries;
    private final Class<T> discoveryType;

    /**
     * Creates a new discoverer instance.
     *
     * @param discoveries   the {@link Replaceable} instances the discoverer contains and provides
     * @param discoveryType the type of the {@link Replaceable} instances
     */
    protected Discoverer(Set<T> discoveries, Class<T> discoveryType) {
        this.discoveries = Set.copyOf(discoveries);
        this.discoveryType = Objects.requireNonNull(discoveryType);
    }

    /**
     * Gets the {@link Replaceable} instances the discoverer contains.
     *
     * @return the contained {@link Replaceable} instances.
     */
    public Set<T> getDiscoveries() {
        return discoveries;
    }

    /**
     * Gets the type of {@link Replaceable} instances the discoverer contains.
     *
     * @return the type of the contained {@link Replaceable} instances.
     */
    public Class<T> getDiscoveryType() {
        return discoveryType;
    }
}
