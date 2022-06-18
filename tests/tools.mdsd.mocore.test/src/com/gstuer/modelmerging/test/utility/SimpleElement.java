package com.gstuer.modelmerging.test.utility;

import com.gstuer.modelmerging.framework.surrogate.Element;

public class SimpleElement extends Element<Long> {
    private static long nextIdentifier;

    public SimpleElement(boolean isPlaceholder) {
        super(nextIdentifier++, isPlaceholder);
    }
}
