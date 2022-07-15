package com.gstuer.modelmerging.instance.pcm.transformation;

import org.palladiosimulator.pcm.allocation.Allocation;

import com.gstuer.modelmerging.framework.transformation.TransformerTest;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;

public class AllocationTransformerTest extends TransformerTest<AllocationTransformer, PcmSurrogate, Allocation> {
    // TODO Implement Test Methods

    @Override
    protected AllocationTransformer createTransformer() {
        return new AllocationTransformer();
    }

    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }
}
