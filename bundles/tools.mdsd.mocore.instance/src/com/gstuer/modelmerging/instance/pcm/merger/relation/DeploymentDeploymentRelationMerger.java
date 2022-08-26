package com.gstuer.modelmerging.instance.pcm.merger.relation;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.gstuer.modelmerging.framework.merger.RelationMerger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.LinkResourceSpecification;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAllocationRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAssemblyRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.DeploymentDeploymentRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.LinkResourceSpecificationRelation;

public class DeploymentDeploymentRelationMerger extends RelationMerger<PcmSurrogate, DeploymentDeploymentRelation> {
    public DeploymentDeploymentRelationMerger(PcmSurrogate model) {
        super(model, DeploymentDeploymentRelation.class);
    }

    @Override
    protected void refine(DeploymentDeploymentRelation discovery) {
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

        super.refine(discovery);
    }

    @Override
    protected void replaceIndirectPlaceholders(DeploymentDeploymentRelation discovery) {
        /*
         * TL;DR Indirect refinement of depl->depl relations is disabled because it leads to information loss.
         *
         * In a real system each component has to be allocated to a minimum of one container. Otherwise, the component
         * is not a working part of the running system. As a consequence, a placeholder container is created for each
         * component without a real container. This placeholder cannot be replaced by any depl->depl relation with the
         * same source or destination because the replacement would also imply that the component of the container is
         * deployed on the new "replacement" container. This may be true but is false in general. Therefore, the
         * replacement of the placeholder container has to be triggered from a lower architectural "layer".
         *
         * Example: Let's assume we have two components called A and B. When we add A and B to the model, we also add a
         * placeholder container A_Cont for A and B_Cont for B. Moreover, a component assembly called A->B is added to
         * the model leading to a A_Cont<->B_Cont deployment relation. The resulting model is in a hazard-state due to
         * the following problem: If we now add another deployment relation, B_Cont<->C_Cont for example, the indirect
         * refinement would assume that C_Cont and A_Cont are the same and initiate the replacement of A_Cont to C_Cont.
         * The implication of this replacement is that A and the components of C_Cont are deployed on the same
         * container. In general, this implication is not true. Consequently, a replacement of A_Cont may only be
         * initiated by a lower-level relation. In the PCM case this relation is the component allocation relation.
         */
    }
}
