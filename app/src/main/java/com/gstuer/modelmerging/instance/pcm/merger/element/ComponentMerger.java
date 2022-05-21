package com.gstuer.modelmerging.instance.pcm.merger.element;

import com.gstuer.modelmerging.framework.merger.Merger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;

public class ComponentMerger extends Merger<PcmSurrogate, Component> {
    public ComponentMerger(PcmSurrogate model) {
        super(model, Component.class);
    }

    @Override
    protected void refine(Component discovery) {
        // TODO Add refinement steps
    }
}
