package com.gstuer.modelmerging.instance.pcm.merger.element;

import java.util.List;

import com.gstuer.modelmerging.framework.merger.Merger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentDeploymentRelation;

public class ComponentMerger extends Merger<PcmSurrogate, Component> {
    public ComponentMerger(PcmSurrogate model) {
        super(model, Component.class);
    }

    @Override
    protected void refine(Component discovery) {
        List<ComponentDeploymentRelation> deploymentRelations = getModel().getByType(ComponentDeploymentRelation.class);
        deploymentRelations.removeIf(relation -> !relation.getSource().equals(discovery));

        if (deploymentRelations.isEmpty()) {
            Deployment deployment = Deployment.getUniquePlaceholder();
            ComponentDeploymentRelation relation = new ComponentDeploymentRelation(discovery, deployment, true);
            addImplication(relation);
        }
    }
}
