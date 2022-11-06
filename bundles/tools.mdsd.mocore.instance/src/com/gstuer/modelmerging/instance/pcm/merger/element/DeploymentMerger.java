package com.gstuer.modelmerging.instance.pcm.merger.element;

import com.gstuer.modelmerging.framework.merger.Processor;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;

public class DeploymentMerger extends Processor<PcmSurrogate, Deployment> {
    public DeploymentMerger(PcmSurrogate model) {
        super(model, Deployment.class);
    }

    @Override
    protected void refine(Deployment discovery) {
        // No refinement needed when adding a deployment element
    }
}
