package com.gstuer.modelmerging.instance.pcm.merger.element;

import java.util.List;

import com.gstuer.modelmerging.framework.merger.Merger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Interface;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ProvidesRelation;

public class InterfaceMerger extends Merger<PcmSurrogate, Interface> {
    public InterfaceMerger(PcmSurrogate model) {
        super(model, Interface.class);
    }

    @Override
    protected void refine(Interface discovery) {
        List<ProvidesRelation> providesRelations = getModel().getByType(ProvidesRelation.class);
        providesRelations.removeIf(relation -> !relation.getDestination().equals(discovery));
        List<ProvidesRelation> requiresRelations = getModel().getByType(ProvidesRelation.class);
        requiresRelations.removeIf(relation -> !relation.getDestination().equals(discovery));

        if (providesRelations.isEmpty() && requiresRelations.isEmpty()) {
            Component component = Component.getUniquePlaceholder();
            ProvidesRelation relation = new ProvidesRelation(component, discovery, true);
            addImplication(relation);
        }
    }

}
