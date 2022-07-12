package com.gstuer.modelmerging.instance.pcm.merger.relation;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.gstuer.modelmerging.framework.merger.RelationMerger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Interface;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.LinkResourceSpecification;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAllocationRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAssemblyRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.DeploymentDeploymentRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceProvisionRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceRequirementRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.LinkResourceSpecificationRelation;

public class DeploymentDeploymentRelationMerger extends RelationMerger<PcmSurrogate, DeploymentDeploymentRelation> {
    public DeploymentDeploymentRelationMerger(PcmSurrogate model) {
        super(model, DeploymentDeploymentRelation.class);
    }

    @Override
    protected void refine(DeploymentDeploymentRelation discovery) {
        super.refine(discovery);

        // Check whether a linking resource specification already exists for the link to be merged
        List<LinkResourceSpecificationRelation> specifications = this.getModel()
                .getByType(LinkResourceSpecificationRelation.class);
        specifications.removeIf(specification -> !Objects.equals(discovery, specification.getDestination()));
        if (specifications.isEmpty()) {
            // Add implicit placeholder specification, if no specification is found for this deployment link
            LinkResourceSpecification placeholderSpecification = LinkResourceSpecification.getUniquePlaceholder();
            LinkResourceSpecificationRelation implicitRelation = new LinkResourceSpecificationRelation(
                    placeholderSpecification, discovery, true);
            this.addImplication(implicitRelation);
        }

        // Get assemblies and allocations from model for component level implications
        List<ComponentAssemblyRelation> assemblies = this.getModel().getByType(ComponentAssemblyRelation.class);
        List<ComponentAllocationRelation> allocations = this.getModel().getByType(ComponentAllocationRelation.class);
        List<Component> sourceAllocations = allocations.stream()
                .filter(allocation -> allocation.getDestination().equals(discovery.getSource()))
                .map(ComponentAllocationRelation::getSource)
                .collect(Collectors.toList());
        List<Component> destinationAllocations = allocations.stream()
                .filter(allocation -> allocation.getDestination().equals(discovery.getDestination()))
                .map(ComponentAllocationRelation::getSource)
                .collect(Collectors.toList());

        // Check whether a component assembly exists between the linked containers
        boolean existsAssembly = false;
        for (ComponentAssemblyRelation assembly : assemblies) {
            Component assemblySource = assembly.getSource().getSource();
            Component assemblyDestination = assembly.getDestination().getSource();

            // Container links are bi-directional => Parallel or inverse assemblies are valid
            boolean isParallelAssembly = sourceAllocations.contains(assemblySource)
                    && destinationAllocations.contains(assemblyDestination);
            boolean isInverseAssembly = sourceAllocations.contains(assemblyDestination)
                    && destinationAllocations.contains(assemblySource);
            if (isParallelAssembly || isInverseAssembly) {
                existsAssembly = true;
                break;
            }
        }

        // Add placeholder components and an assembly with none exists between the containers
        if (!existsAssembly) {
            Component providingComponent = Component.getUniquePlaceholder();
            Component requiringComponent = Component.getUniquePlaceholder();
            Interface assemblyInterface = Interface.getUniquePlaceholder();
            InterfaceProvisionRelation provider = new InterfaceProvisionRelation(providingComponent,
                    assemblyInterface, true);
            InterfaceRequirementRelation consumer = new InterfaceRequirementRelation(requiringComponent,
                    assemblyInterface, true);
            ComponentAssemblyRelation assembly = new ComponentAssemblyRelation(provider, consumer, true);
            this.addImplication(assembly);
        }
    }
}
