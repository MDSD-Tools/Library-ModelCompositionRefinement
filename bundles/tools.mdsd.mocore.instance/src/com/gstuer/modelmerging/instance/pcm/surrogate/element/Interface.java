package com.gstuer.modelmerging.instance.pcm.surrogate.element;

import com.gstuer.modelmerging.framework.surrogate.Element;

// TODO Change generic to real palladio component
public class Interface extends Element<String> {
    public Interface(String value, boolean isPlaceholder) {
        super(value, isPlaceholder);
    }

    public static Interface getUniquePlaceholder() {
        String identifier = "Placeholder_" + getUniqueValue();
        return new Interface(identifier, true);
    }
}
