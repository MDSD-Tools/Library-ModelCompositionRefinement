/* Copyright (C) 2023 Moritz Gstuer - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the EUPL 1.2 license.
 *
 * You should have received a copy of the EUPL 1.2 license
 * with this file. If not, please visit:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */
package tools.mdsd.mocore.utility;

public final class IdentifierGenerator {
    private static long nextUniqueValue;

    private IdentifierGenerator() {
        // Utility class
        throw new IllegalStateException();
    }

    public static String getUniqueIdentifier() {
        return String.valueOf("ID_" + getUniqueLongValue());
    }

    public static long getUniqueLongValue() {
        return nextUniqueValue++;
    }
}
