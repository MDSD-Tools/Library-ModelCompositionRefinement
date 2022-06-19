package com.gstuer.modelmerging.instance.pcm.merger;

import com.gstuer.modelmerging.framework.merger.MergerTest;
import com.gstuer.modelmerging.instance.pcm.merger.element.ComponentMerger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;

public class ComponentMergerTest extends MergerTest<ComponentMerger, PcmSurrogate, Component> {
    @Override
    protected ComponentMerger createMerger(PcmSurrogate model) {
        return new ComponentMerger(model);
    }

    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }

    @Override
    protected Component createUniqueReplaceable() {
        return Component.getUniquePlaceholder();
    }
}
