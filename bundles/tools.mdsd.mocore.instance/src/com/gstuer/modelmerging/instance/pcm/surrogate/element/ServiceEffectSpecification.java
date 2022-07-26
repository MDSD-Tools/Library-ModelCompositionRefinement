package com.gstuer.modelmerging.instance.pcm.surrogate.element;

import org.palladiosimulator.generator.fluent.repository.factory.FluentRepositoryFactory;
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;

import com.gstuer.modelmerging.framework.surrogate.Element;

public class ServiceEffectSpecification extends Element<ResourceDemandingSEFF> {
    public ServiceEffectSpecification(ResourceDemandingSEFF value, boolean isPlaceholder) {
        super(value, isPlaceholder);
    }

    public static ServiceEffectSpecification getUniquePlaceholder() {
        ResourceDemandingSEFF value = new FluentRepositoryFactory().newSeff()
                .withSeffBehaviour().withStartAction().followedBy().stopAction().createBehaviourNow().buildRDSeff();
        return new ServiceEffectSpecification(value, true);
    }
}
