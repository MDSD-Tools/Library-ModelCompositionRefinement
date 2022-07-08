package com.gstuer.modelmerging.instance.pcm.transformation;

import org.palladiosimulator.generator.fluent.resourceenvironment.api.IResourceEnvironment;
import org.palladiosimulator.generator.fluent.resourceenvironment.factory.FluentResourceEnvironmentFactory;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import com.gstuer.modelmerging.framework.transformation.Transformer;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;

public class ResourceEnvironmentTransformer implements Transformer<PcmSurrogate, ResourceEnvironment> {

    @Override
    public ResourceEnvironment transform(PcmSurrogate model) {
        FluentResourceEnvironmentFactory resourceEnvironmentFactory = new FluentResourceEnvironmentFactory();
        IResourceEnvironment fluentResourceEnvironment = resourceEnvironmentFactory.newResourceEnvironment();

        // TODO Add resource containers (deployments)

        // TODO Add linking resources (deployment <-> deployment)

        return fluentResourceEnvironment.createResourceEnvironmentNow();
    }
}
