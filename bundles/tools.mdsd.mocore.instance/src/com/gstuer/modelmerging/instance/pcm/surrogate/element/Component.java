package com.gstuer.modelmerging.instance.pcm.surrogate.element;

import org.palladiosimulator.generator.fluent.repository.factory.FluentRepositoryFactory;
import org.palladiosimulator.pcm.repository.BasicComponent;

import com.gstuer.modelmerging.framework.surrogate.Element;

public class Component extends Element<BasicComponent> {
    public Component(BasicComponent value, boolean isPlaceholder) {
        super(value, isPlaceholder);
    }

    public static Component getUniquePlaceholder() {
        String identifier = "Placeholder_" + getUniqueValue();
        BasicComponent value = new FluentRepositoryFactory().newBasicComponent().withName(identifier).build();
        return new Component(value, true);
    }
}
