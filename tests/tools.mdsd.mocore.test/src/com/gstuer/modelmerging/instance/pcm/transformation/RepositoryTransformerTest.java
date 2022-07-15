package com.gstuer.modelmerging.instance.pcm.transformation;

import org.palladiosimulator.pcm.repository.Repository;

import com.gstuer.modelmerging.framework.transformation.TransformerTest;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;

public class RepositoryTransformerTest extends TransformerTest<RepositoryTransformer, PcmSurrogate, Repository> {
    // TODO Implement Test Methods

    @Override
    protected RepositoryTransformer createTransformer() {
        return new RepositoryTransformer();
    }

    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }
}
