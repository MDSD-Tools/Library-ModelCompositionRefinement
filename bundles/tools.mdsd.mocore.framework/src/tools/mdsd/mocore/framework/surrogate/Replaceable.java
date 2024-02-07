/* Copyright (C) 2023 Moritz Gstuer - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the EUPL 1.2 license.
 *
 * You should have received a copy of the EUPL 1.2 license
 * with this file. If not, please visit:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */
package tools.mdsd.mocore.framework.surrogate;

import java.util.Objects;

public abstract class Replaceable {
    private final boolean isPlaceholder;

    protected Replaceable(boolean isPlaceholder) {
        this.isPlaceholder = isPlaceholder;
    }

    public boolean isPlaceholder() {
        return isPlaceholder;
    }

    public abstract boolean includes(Replaceable replaceable);

    public abstract <T extends Replaceable> Replaceable replace(T original, T replacement);

    public abstract boolean isPlaceholderOf(Replaceable replaceable);

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Replaceable replaceable = (Replaceable) object;
        return isPlaceholder == replaceable.isPlaceholder;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isPlaceholder);
    }
}
