package com.gstuer.modelmerging.instance.pcm.merger.relation;

import java.util.List;
import java.util.stream.Collectors;

import com.gstuer.modelmerging.framework.merger.RelationMerger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;
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
        List<ComponentAllocationRelation> allocations = this.getModel().getByType(ComponentAllocationRelation.class);
        List<Deployment> providerAllocations = allocations.stream()
                .filter(allocation -> allocation.getSource().equals(provider))
                .map(ComponentAllocationRelation::getDestination)
                .collect(Collectors.toList());
        List<Deployment> consumerAllocations = allocations.stream()
                .filter(allocation -> allocation.getSource().equals(consumer))
                .map(ComponentAllocationRelation::getDestination)
                .collect(Collectors.toList());

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
    }
}
