package com.gstuer.modelmerging.instance.pcm.surrogate.element;

import org.palladiosimulator.generator.fluent.repository.factory.FluentRepositoryFactory;
import org.palladiosimulator.pcm.repository.OperationInterface;

import com.gstuer.modelmerging.framework.surrogate.Element;

public class Interface extends Element<OperationInterface> {
    public Interface(OperationInterface value, boolean isPlaceholder) {
        super(value, isPlaceholder);
    }

    public static Interface getUniquePlaceholder() {
        String identifier = "Placeholder_" + getUniqueValue();
        OperationInterface value = new FluentRepositoryFactory().newOperationInterface().withName(identifier).build();
        return new Interface(value, true);
    }
}
