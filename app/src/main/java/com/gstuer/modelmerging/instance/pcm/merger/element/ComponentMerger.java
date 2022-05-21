package com.gstuer.modelmerging.instance.pcm.merger.element;

import com.gstuer.modelmerging.framework.merger.Merger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentDeploymentRelation;

import java.util.List;
import java.util.stream.Collectors;

public class ComponentMerger extends Merger<PcmSurrogate, Component> {
    public ComponentMerger(PcmSurrogate model) {
        super(model, Component.class);
    }

    @Override
    protected void refine(Component discovery) {
        List<ComponentDeploymentRelation> deploymentRelations = getModel().getByType(ComponentDeploymentRelation.class)
                .stream()
                .dropWhile(relation -> !relation.getSource().equals(discovery))
                .collect(Collectors.toList());
        if (deploymentRelations.isEmpty()) {
            // TODO Replace with concrete pcm placeholder deployment
            Deployment deployment = new Deployment("Placeholder", true);
            ComponentDeploymentRelation relation = new ComponentDeploymentRelation(discovery, deployment, true);
            addImplication(relation);
        }
    }
}
