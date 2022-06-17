package com.gstuer.modelmerging.instance.pcm.surrogate.element;

import com.gstuer.modelmerging.framework.surrogate.Element;

// TODO Change generic to real palladio component
public class Component extends Element<String> {
    public Component(String value, boolean isPlaceholder) {
        super(value, isPlaceholder);
    }

    public static Component getUniquePlaceholder() {
        String identifier = "Placeholder_" + getUniqueValue();
        return new Component(identifier, true);
    }
}
