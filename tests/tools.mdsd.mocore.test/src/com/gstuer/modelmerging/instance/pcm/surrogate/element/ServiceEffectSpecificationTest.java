package com.gstuer.modelmerging.instance.pcm.surrogate.element;

import com.gstuer.modelmerging.framework.surrogate.ElementTest;
import com.gstuer.modelmerging.test.utility.ElementFactory;

public class ServiceEffectSpecificationTest
        extends ElementTest<ServiceEffectSpecification, org.palladiosimulator.pcm.seff.ServiceEffectSpecification> {
    @Override
    protected ServiceEffectSpecification createElement(org.palladiosimulator.pcm.seff.ServiceEffectSpecification value,
            boolean isPlaceholder) {
        return new ServiceEffectSpecification(value, isPlaceholder);
    }

    @Override
    protected org.palladiosimulator.pcm.seff.ServiceEffectSpecification getUniqueValue() {
        return ElementFactory.createUniqueServiceEffectSpecification(false).getValue();
    }

    @Override
    protected ServiceEffectSpecification getUniqueNonPlaceholder() {
        return new ServiceEffectSpecification(getUniqueValue(), false);
    }

    @Override
    protected ServiceEffectSpecification getPlaceholderOf(ServiceEffectSpecification replaceable) {
        return new ServiceEffectSpecification(replaceable.getValue(), true);
    }
}
