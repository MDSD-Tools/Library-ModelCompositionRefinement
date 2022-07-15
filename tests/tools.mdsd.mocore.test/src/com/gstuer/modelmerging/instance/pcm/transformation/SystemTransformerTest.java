package com.gstuer.modelmerging.instance.pcm.transformation;

import org.palladiosimulator.pcm.system.System;

import com.gstuer.modelmerging.framework.transformation.TransformerTest;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;

public class SystemTransformerTest extends TransformerTest<SystemTransformer, PcmSurrogate, System> {
    // TODO Implement Test Methods

    @Override
    protected SystemTransformer createTransformer() {
        return new SystemTransformer();
    }

    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }
}
