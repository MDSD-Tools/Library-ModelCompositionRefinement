package com.gstuer.modelmerging.instance.pcm.merger.element;

import java.util.List;

import com.gstuer.modelmerging.framework.merger.Merger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Interface;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceProvisionRelation;

public class InterfaceMerger extends Merger<PcmSurrogate, Interface> {
    public InterfaceMerger(PcmSurrogate model) {
        super(model, Interface.class);
    }

    @Override
    protected void refine(Interface discovery) {
        List<InterfaceProvisionRelation> providesRelations = getModel().getByType(InterfaceProvisionRelation.class);
        providesRelations.removeIf(relation -> !relation.getDestination().equals(discovery));
        List<InterfaceProvisionRelation> requiresRelations = getModel().getByType(InterfaceProvisionRelation.class);
        requiresRelations.removeIf(relation -> !relation.getDestination().equals(discovery));

        if (providesRelations.isEmpty() && requiresRelations.isEmpty()) {
            Component component = Component.getUniquePlaceholder();
            InterfaceProvisionRelation relation = new InterfaceProvisionRelation(component, discovery, true);
            addImplication(relation);
        }
    }

}
