package com.gstuer.modelmerging.instance.pcm.orchestration;

import com.gstuer.modelmerging.framework.orchestration.Orchestrator;
import com.gstuer.modelmerging.instance.pcm.merger.element.ComponentMerger;
import com.gstuer.modelmerging.instance.pcm.merger.element.DeploymentMerger;
import com.gstuer.modelmerging.instance.pcm.merger.relation.ComponentComponentRelationMerger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;

public class PcmOrchestrator extends Orchestrator<PcmSurrogate> {
    public PcmOrchestrator(PcmSurrogate model) {
        super(model, new ComponentMerger(model), new DeploymentMerger(model),
                new ComponentComponentRelationMerger(model), new DeploymentMerger(model));
    }

    public PcmOrchestrator() {
        this(new PcmSurrogate());
    }
}
