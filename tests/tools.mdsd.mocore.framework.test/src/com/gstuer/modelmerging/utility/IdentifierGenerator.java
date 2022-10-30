package com.gstuer.modelmerging.utility;

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
