package com.gstuer.modelmerging.instance.pcm.transformation;

import java.util.Objects;

import org.palladiosimulator.generator.fluent.resourceenvironment.api.IResourceEnvironment;
import org.palladiosimulator.generator.fluent.resourceenvironment.factory.FluentResourceEnvironmentFactory;
import org.palladiosimulator.generator.fluent.resourceenvironment.structure.LinkingResourceCreator;
import org.palladiosimulator.generator.fluent.resourceenvironment.structure.ResourceContainerCreator;
import org.palladiosimulator.pcm.resourceenvironment.CommunicationLinkResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import com.gstuer.modelmerging.framework.transformation.Transformer;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.DeploymentDeploymentRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.LinkResourceSpecificationRelation;

public class ResourceEnvironmentTransformer implements Transformer<PcmSurrogate, ResourceEnvironment> {
    private static final String LINKING_RESSOURCE_NAME_PATTERN = "%s %s Link";

    @Override
    public ResourceEnvironment transform(PcmSurrogate model) {
        FluentResourceEnvironmentFactory resourceEnvironmentFactory = new FluentResourceEnvironmentFactory();
        IResourceEnvironment fluentResourceEnvironment = resourceEnvironmentFactory.newResourceEnvironment();

        // Add resource containers to resource environment
        for (Deployment deployment : model.getByType(Deployment.class)) {
            ResourceContainerCreator containerCreator = getContainerCreator(resourceEnvironmentFactory, deployment);
            fluentResourceEnvironment.addToResourceEnvironment(containerCreator);
        }

        // Add linking resources (specification <-> [deployment <-> deployment]) to resource environment
        for (LinkResourceSpecificationRelation linkingRelation : model
                .getByType(LinkResourceSpecificationRelation.class)) {
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

        // Add linking resource specifications to PCM linking resources
        for (LinkResourceSpecificationRelation linkingRelation : model
                .getByType(LinkResourceSpecificationRelation.class)) {
            for (LinkingResource linkingResource : resourceEnvironment.getLinkingResources__ResourceEnvironment()) {
                if (Objects.equals(getLinkingResourceName(linkingRelation), linkingResource.getEntityName())) {
                    CommunicationLinkResourceSpecification specification = linkingRelation.getSource().getValue();
                    linkingResource.setCommunicationLinkResourceSpecifications_LinkingResource(specification);
                }
            }
        }

        return resourceEnvironment;
    }

    protected static String getLinkingResourceName(LinkResourceSpecificationRelation linkingRelation) {
        DeploymentDeploymentRelation deploymentRelation = linkingRelation.getDestination();
        String sourceName = deploymentRelation.getSource().getValue().getEntityName();
        String destinationName = deploymentRelation.getDestination().getValue().getEntityName();
        return String.format(LINKING_RESSOURCE_NAME_PATTERN, sourceName, destinationName);
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
            LinkResourceSpecificationRelation linkingRelation) {
        // Create a linking resource creator w/o specifications due to missing fluentApi copy support
        String entityName = getLinkingResourceName(linkingRelation);
        LinkingResourceCreator creator = fluentFactory.newLinkingResource().withName(entityName);
        return creator;
    }
}
