package com.gstuer.modelmerging.instance.pcm.transformation;

import org.palladiosimulator.generator.fluent.resourceenvironment.api.IResourceEnvironment;
import org.palladiosimulator.generator.fluent.resourceenvironment.factory.FluentResourceEnvironmentFactory;
import org.palladiosimulator.generator.fluent.resourceenvironment.structure.LinkingResourceCreator;
import org.palladiosimulator.generator.fluent.resourceenvironment.structure.ResourceContainerCreator;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import com.gstuer.modelmerging.framework.transformation.Transformer;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.DeploymentDeploymentRelation;

public class ResourceEnvironmentTransformer implements Transformer<PcmSurrogate, ResourceEnvironment> {
    @Override
    public ResourceEnvironment transform(PcmSurrogate model) {
        FluentResourceEnvironmentFactory resourceEnvironmentFactory = new FluentResourceEnvironmentFactory();
        IResourceEnvironment fluentResourceEnvironment = resourceEnvironmentFactory.newResourceEnvironment();

        // Add resource containers to resource environment
        for (Deployment deployment : model.getByType(Deployment.class)) {
            ResourceContainerCreator containerCreator = getContainerCreator(resourceEnvironmentFactory, deployment);
            fluentResourceEnvironment.addToResourceEnvironment(containerCreator);
        }

        // Add linking resources (deployment <-> deployment) to resource environment
        for (DeploymentDeploymentRelation linkingRelation : model.getByType(DeploymentDeploymentRelation.class)) {
            LinkingResourceCreator linkingResourceCreator = getLinkingResourceCreator(resourceEnvironmentFactory,
                    linkingRelation);
            fluentResourceEnvironment.addToResourceEnvironment(linkingResourceCreator);
        }

        // Create PCM resource environment
        ResourceEnvironment resourceEnvironment = fluentResourceEnvironment.createResourceEnvironmentNow();

        // Copy resource specifications from old to new containers
        for (ResourceContainer container : resourceEnvironment.getResourceContainer_ResourceEnvironment()) {
            for (Deployment deployment : model.getByType(Deployment.class)) {
                ResourceContainer wrappedContainer = deployment.getValue();
                if (container.getEntityName().equals(wrappedContainer.getEntityName())) {
                    container.getActiveResourceSpecifications_ResourceContainer()
                            .addAll(wrappedContainer.getActiveResourceSpecifications_ResourceContainer());
                    container.getHddResourceSpecifications().addAll(wrappedContainer.getHddResourceSpecifications());
                }
            }
        }

        return resourceEnvironment;
    }

    private ResourceContainerCreator getContainerCreator(FluentResourceEnvironmentFactory fluentFactory,
            Deployment deployment) {
        ResourceContainer wrappedContainer = deployment.getValue();

        // Create a container creator instance w/o processing specifications due to missing fluentApi copy support
        ResourceContainerCreator containerCreator = fluentFactory.newResourceContainer()
                .withName(wrappedContainer.getEntityName());
        return containerCreator;
    }

    private LinkingResourceCreator getLinkingResourceCreator(FluentResourceEnvironmentFactory fluentFactory,
            DeploymentDeploymentRelation linkingRelation) {
        // TODO Return copied linking resource
        return null;
    }
}
