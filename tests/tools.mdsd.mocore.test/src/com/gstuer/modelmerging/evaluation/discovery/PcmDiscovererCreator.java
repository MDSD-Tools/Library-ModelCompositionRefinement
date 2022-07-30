package com.gstuer.modelmerging.evaluation.discovery;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.palladiosimulator.generator.fluent.repository.factory.FluentRepositoryFactory;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

import com.gstuer.modelmerging.framework.discovery.Discoverer;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Interface;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.LinkResourceSpecification;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAllocationRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAssemblyRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.DeploymentDeploymentRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceProvisionRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceRequirementRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.LinkResourceSpecificationRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.SignatureProvisionRelation;
import com.gstuer.modelmerging.test.utility.IdentifierGenerator;

public class PcmDiscovererCreator {
    private final Repository repository;
    private final System system;
    private final Allocation allocation;
    private final ResourceEnvironment resourceEnvironment;

    public PcmDiscovererCreator(Repository repository, System system, Allocation allocation,
            ResourceEnvironment resourceEnvironment) {
        this.repository = repository;
        this.system = system;
        this.allocation = allocation;
        this.resourceEnvironment = resourceEnvironment;
    }

    public Collection<Discoverer<?>> createDiscoverers() {
        Collection<Discoverer<?>> discoverers = new LinkedList<>();
        discoverers.addAll(createDiscoverersFromRepository());
        discoverers.addAll(createDiscoverersFromSystem());
        discoverers.addAll(createDiscoverersFromAllocation());
        discoverers.addAll(createDiscoverersFromResourceEnvironment());
        return discoverers;
    }

    public Collection<Discoverer<?>> createDiscoverersFromRepository() {
        Set<Component> components = new HashSet<>();
        Set<Interface> interfaces = new HashSet<>();
        Set<SignatureProvisionRelation> signatureProvisions = new HashSet<>();
        Set<InterfaceProvisionRelation> interfaceProvisions = new HashSet<>();
        Set<InterfaceRequirementRelation> interfaceRequirements = new HashSet<>();

        // TODO
        return null;
    }

    public Collection<Discoverer<?>> createDiscoverersFromSystem() {
        Set<ComponentAssemblyRelation> assemblyRelations = new HashSet<>();

        // Find assembly connectors within system
        List<AssemblyConnector> assemblyConnectors = system.getConnectors__ComposedStructure().stream()
                .filter(connector -> connector instanceof AssemblyConnector)
                .map(connector -> (AssemblyConnector) connector)
                .collect(Collectors.toList());
        for (AssemblyConnector connector : assemblyConnectors) {
            RepositoryComponent connectorProvider = connector.getProvidingAssemblyContext_AssemblyConnector()
                    .getEncapsulatedComponent__AssemblyContext();
            RepositoryComponent connectorConsumer = connector.getRequiringAssemblyContext_AssemblyConnector()
                    .getEncapsulatedComponent__AssemblyContext();
            OperationInterface connectorProviderConsumerInterface = connector.getProvidedRole_AssemblyConnector()
                    .getProvidedInterface__OperationProvidedRole();

            Component provider = createComponentFromRepositoryComponent(connectorProvider);
            Component consumer = createComponentFromRepositoryComponent(connectorConsumer);
            Interface providerConsumerInterface = new Interface(connectorProviderConsumerInterface, false);
            InterfaceProvisionRelation interfaceProvision = new InterfaceProvisionRelation(provider,
                    providerConsumerInterface, false);
            InterfaceRequirementRelation interfaceRequirement = new InterfaceRequirementRelation(consumer,
                    providerConsumerInterface, false);
            ComponentAssemblyRelation assemblyRelation = new ComponentAssemblyRelation(interfaceProvision,
                    interfaceRequirement, false);
            assemblyRelations.add(assemblyRelation);
        }

        PcmDiscoverer<ComponentAssemblyRelation> assemblyRelationDiscoverer = new PcmDiscoverer<>(assemblyRelations,
                ComponentAssemblyRelation.class);
        return List.of(assemblyRelationDiscoverer);
    }

    public Collection<Discoverer<?>> createDiscoverersFromAllocation() {
        Set<ComponentAllocationRelation> allocations = new HashSet<>();
        for (AllocationContext allocationContext : this.allocation.getAllocationContexts_Allocation()) {
            AssemblyContext assemblyContext = allocationContext.getAssemblyContext_AllocationContext();
            ResourceContainer container = allocationContext.getResourceContainer_AllocationContext();

            Component component = createComponentFromRepositoryComponent(
                    assemblyContext.getEncapsulatedComponent__AssemblyContext());
            Deployment deployment = new Deployment(container, false);
            ComponentAllocationRelation componentAllocationRelation = new ComponentAllocationRelation(component,
                    deployment, false);
            allocations.add(componentAllocationRelation);
        }

        PcmDiscoverer<ComponentAllocationRelation> allocationDiscoverer = new PcmDiscoverer<>(allocations,
                ComponentAllocationRelation.class);
        return List.of(allocationDiscoverer);
    }

    public Collection<Discoverer<?>> createDiscoverersFromResourceEnvironment() {
        Set<Deployment> containers = new HashSet<>();
        Set<LinkResourceSpecificationRelation> linkRelations = new HashSet<>();

        // Find all containers in resource environment
        for (ResourceContainer container : this.resourceEnvironment.getResourceContainer_ResourceEnvironment()) {
            containers.add(new Deployment(container, false));
        }

        // Find all linking resources and create relation for each connected container pair
        for (LinkingResource linkingResource : this.resourceEnvironment.getLinkingResources__ResourceEnvironment()) {
            for (ResourceContainer source : linkingResource.getConnectedResourceContainers_LinkingResource()) {
                for (ResourceContainer destination : linkingResource.getConnectedResourceContainers_LinkingResource()) {
                    if (!Objects.equals(source, destination)) {
                        DeploymentDeploymentRelation deploymentRelation = new DeploymentDeploymentRelation(
                                new Deployment(source, false), new Deployment(destination, false), false);
                        LinkResourceSpecification linkSpecification = new LinkResourceSpecification(
                                linkingResource.getCommunicationLinkResourceSpecifications_LinkingResource(), false);
                        LinkResourceSpecificationRelation linkRelation = new LinkResourceSpecificationRelation(
                                linkSpecification, deploymentRelation, false);
                        linkRelations.add(linkRelation);
                    }
                }
            }
        }

        PcmDiscoverer<Deployment> containerDiscoverer = new PcmDiscoverer<>(containers, Deployment.class);
        PcmDiscoverer<LinkResourceSpecificationRelation> linkRelationDiscoverer = new PcmDiscoverer<>(linkRelations,
                LinkResourceSpecificationRelation.class);
        return List.of(containerDiscoverer, linkRelationDiscoverer);
    }

    private Component createComponentFromRepositoryComponent(RepositoryComponent repositoryComponent) {
        if (repositoryComponent instanceof BasicComponent) {
            return new Component((BasicComponent) repositoryComponent, false);
        }
        String name = IdentifierGenerator.getUniqueIdentifier();
        BasicComponent value = new FluentRepositoryFactory().newBasicComponent().withName(name).build();
        return new Component(value, false);
    }
}
