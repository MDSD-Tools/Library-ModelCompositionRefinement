package com.gstuer.modelmerging.instance.pcm.merger;

import com.gstuer.modelmerging.framework.merger.MergerTest;
import com.gstuer.modelmerging.instance.pcm.merger.element.InterfaceMerger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Interface;

public class InterfaceMergerTest extends MergerTest<InterfaceMerger, PcmSurrogate, Interface> {
    @Override
    protected InterfaceMerger createMerger(PcmSurrogate model) {
        return new InterfaceMerger(model);
    }

    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }

    @Override
    protected Interface createUniqueReplaceable() {
        return Interface.getUniquePlaceholder();
    }
}
