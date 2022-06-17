package com.gstuer.modelmerging.instance.pcm.surrogate.element;

import com.gstuer.modelmerging.framework.surrogate.Element;

// TODO Change generic to real palladio component
public class Signature extends Element<String> {
    public Signature(String value, boolean isPlaceholder) {
        super(value, isPlaceholder);
    }

    public static Signature getUniquePlaceholder() {
        String identifier = "Placeholder_" + getUniqueValue();
        return new Signature(identifier, true);
    }
}
