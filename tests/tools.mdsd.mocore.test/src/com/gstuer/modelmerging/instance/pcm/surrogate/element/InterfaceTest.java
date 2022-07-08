package com.gstuer.modelmerging.instance.pcm.surrogate.element;

import org.palladiosimulator.generator.fluent.repository.factory.FluentRepositoryFactory;
import org.palladiosimulator.pcm.repository.OperationInterface;

import com.gstuer.modelmerging.framework.surrogate.ElementTest;
import com.gstuer.modelmerging.test.utility.IdentifierGenerator;

public class InterfaceTest extends ElementTest<Interface, OperationInterface> {
    @Override
    protected Interface createElement(OperationInterface value, boolean isPlaceholder) {
        return new Interface(value, isPlaceholder);
    }

    @Override
    protected OperationInterface getUniqueValue() {
        String identifier = IdentifierGenerator.getUniqueIdentifier();
        OperationInterface value = new FluentRepositoryFactory().newOperationInterface().withName(identifier).build();
        return value;
    }

    @Override
    protected Interface getUniqueNonPlaceholder() {
        return new Interface(getUniqueValue(), false);
    }

    @Override
    protected Interface getPlaceholderOf(Interface replaceable) {
        return new Interface(replaceable.getValue(), true);
    }
}
