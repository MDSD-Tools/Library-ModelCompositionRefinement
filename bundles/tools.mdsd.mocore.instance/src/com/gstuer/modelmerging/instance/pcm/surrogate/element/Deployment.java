package com.gstuer.modelmerging.instance.pcm.surrogate.element;

import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;

import com.gstuer.modelmerging.framework.surrogate.Element;

public class Deployment extends Element<ResourceContainer> {
    public Deployment(ResourceContainer value, boolean isPlaceholder) {
        super(value, isPlaceholder);
    }

    public static Deployment getUniquePlaceholder() {
        String identifier = "Placeholder_" + getUniqueValue();
        ResourceContainer value = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
        value.setEntityName(identifier);
        return new Deployment(value, true);
    }
}
