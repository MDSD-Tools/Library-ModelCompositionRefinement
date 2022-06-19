package com.gstuer.modelmerging.instance.pcm.merger;

import com.gstuer.modelmerging.framework.merger.MergerTest;
import com.gstuer.modelmerging.instance.pcm.merger.element.DeploymentMerger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;

public class DeploymentMergerTest extends MergerTest<DeploymentMerger, PcmSurrogate, Deployment> {
    @Override
    protected DeploymentMerger createMerger(PcmSurrogate model) {
        return new DeploymentMerger(model);
    }

    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }

    @Override
    protected Deployment createUniqueReplaceable() {
        return Deployment.getUniquePlaceholder();
    }
}
