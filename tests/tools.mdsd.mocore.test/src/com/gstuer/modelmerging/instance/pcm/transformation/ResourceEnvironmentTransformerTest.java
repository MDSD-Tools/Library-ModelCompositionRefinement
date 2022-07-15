package com.gstuer.modelmerging.instance.pcm.transformation;

import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import com.gstuer.modelmerging.framework.transformation.TransformerTest;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;

public class ResourceEnvironmentTransformerTest
        extends TransformerTest<ResourceEnvironmentTransformer, PcmSurrogate, ResourceEnvironment> {
    // TODO Implement Test Methods

    @Override
    protected ResourceEnvironmentTransformer createTransformer() {
        return new ResourceEnvironmentTransformer();
    }

    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }
}
