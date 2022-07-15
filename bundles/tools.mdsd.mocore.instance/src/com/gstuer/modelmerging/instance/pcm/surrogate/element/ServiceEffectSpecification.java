package com.gstuer.modelmerging.instance.pcm.surrogate.element;

import org.palladiosimulator.generator.fluent.repository.factory.FluentRepositoryFactory;

import com.gstuer.modelmerging.framework.surrogate.Element;

public class ServiceEffectSpecification extends Element<org.palladiosimulator.pcm.seff.ServiceEffectSpecification> {
    public ServiceEffectSpecification(org.palladiosimulator.pcm.seff.ServiceEffectSpecification value,
            boolean isPlaceholder) {
        super(value, isPlaceholder);
    }

    public static ServiceEffectSpecification getUniquePlaceholder() {
        org.palladiosimulator.pcm.seff.ServiceEffectSpecification value = new FluentRepositoryFactory().newSeff()
                .withSeffBehaviour().withStartAction().followedBy().stopAction().createBehaviourNow().build();
        return new ServiceEffectSpecification(value, true);
    }
}
