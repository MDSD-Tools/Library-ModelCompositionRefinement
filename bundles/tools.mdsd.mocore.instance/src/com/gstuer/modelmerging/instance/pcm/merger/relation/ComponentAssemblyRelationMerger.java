package com.gstuer.modelmerging.instance.pcm.merger.relation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.gstuer.modelmerging.framework.merger.RelationMerger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Interface;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAllocationRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAssemblyRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.DeploymentDeploymentRelation;

public class ComponentAssemblyRelationMerger extends RelationMerger<PcmSurrogate, ComponentAssemblyRelation> {
    public ComponentAssemblyRelationMerger(PcmSurrogate model) {
        super(model, ComponentAssemblyRelation.class);
    }

    @Override
    protected void refine(ComponentAssemblyRelation discovery) {
        super.refine(discovery);

        // Identify all allocations of the providing and consuming component in the assembly
        Component provider = discovery.getSource().getSource();
        Component consumer = discovery.getDestination().getSource();
        Interface providerConsumerInterface = discovery.getSource().getDestination();
        List<Deployment> providerAllocations = getAllocatedContainers(provider);
        List<Deployment> consumerAllocations = getAllocatedContainers(consumer);

        // Add link between allocation containers of assembled components if needed
        for (Deployment providerContainer : providerAllocations) {
            for (Deployment consumerContainer : consumerAllocations) {
                if (!providerContainer.equals(consumerContainer)) {
                    // Connect every providing container with each consuming one, except they are the same container
                    DeploymentDeploymentRelation containerLink = new DeploymentDeploymentRelation(providerContainer,
                            consumerContainer, true);
                    this.addImplication(containerLink);
                }
            }
        }

        // Remove component assembly fully-placeholder relation (non-direct & non-indirect)
        List<ComponentAssemblyRelation> assemblies = this.getModel().getByType(ComponentAssemblyRelation.class);
        assemblies.removeIf(assembly -> !assembly.getSource().isPlaceholder()
                || !assembly.getDestination().isPlaceholder());
        for (ComponentAssemblyRelation placeholderAssembly : assemblies) {
            Component source = placeholderAssembly.getSource().getSource();
            Component destination = placeholderAssembly.getDestination().getSource();
            Interface sourceDestinationInterface = placeholderAssembly.getSource().getDestination();
            // Placeholder are unique and can only be allocated to a single container
            Optional<Deployment> optionalSourceContainer = getAllocatedContainers(source)
                    .stream().findFirst();
            Optional<Deployment> optionalDestinationContainer = getAllocatedContainers(destination)
                    .stream().findFirst();

            if (optionalSourceContainer.isPresent() && optionalDestinationContainer.isPresent()) {
                Deployment sourceContainer = optionalSourceContainer.get();
                Deployment destinationContainer = optionalDestinationContainer.get();

                // Container links are bi-directional => Parallel or inverse assemblies are valid
                boolean isParallelAssembly = providerAllocations.contains(sourceContainer)
                        && consumerAllocations.contains(destinationContainer);
                boolean isInverseAssembly = providerAllocations.contains(destinationContainer)
                        && consumerAllocations.contains(sourceContainer);
                if (isParallelAssembly || isInverseAssembly) {
                    this.addImplications(this.getModel().replace(placeholderAssembly, discovery));
                    this.addImplications(this.getModel().replace(source, provider));
                    this.addImplications(this.getModel().replace(destination, consumer));
                    this.addImplications(this.getModel().replace(sourceDestinationInterface,
                            providerConsumerInterface));
                }
            }
        }
    }

    private List<Deployment> getAllocatedContainers(Component component) {
        List<ComponentAllocationRelation> allocations = this.getModel().getByType(ComponentAllocationRelation.class);
        return allocations.stream()
                .filter(allocation -> allocation.getSource().equals(component))
                .map(ComponentAllocationRelation::getDestination)
                .collect(Collectors.toList());
    }
}
