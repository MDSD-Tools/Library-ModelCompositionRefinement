package com.gstuer.modelmerging.instance.pcm.surrogate.element;

import com.gstuer.modelmerging.framework.surrogate.Element;

// TODO Change generic to real palladio deployment
public class Deployment extends Element<String> {
    public Deployment(String value, boolean isPlaceholder) {
        super(value, isPlaceholder);
    }

    public static Deployment getUniquePlaceholder() {
        String identifier = "Placeholder_" + getUniqueValue();
        return new Deployment(identifier, true);
    }
}
