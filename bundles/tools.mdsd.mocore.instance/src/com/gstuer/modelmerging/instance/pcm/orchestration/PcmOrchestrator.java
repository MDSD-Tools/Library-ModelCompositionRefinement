package com.gstuer.modelmerging.instance.pcm.orchestration;

import com.gstuer.modelmerging.framework.orchestration.Orchestrator;
import com.gstuer.modelmerging.instance.pcm.merger.element.ComponentMerger;
import com.gstuer.modelmerging.instance.pcm.merger.element.DeploymentMerger;
import com.gstuer.modelmerging.instance.pcm.merger.relation.ComponentAllocationRelationMerger;
import com.gstuer.modelmerging.instance.pcm.merger.relation.ComponentAssemblyRelationMerger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;

public class PcmOrchestrator extends Orchestrator<PcmSurrogate> {
    public PcmOrchestrator(PcmSurrogate model) {
        // TODO Add missing mergers to constructor
        super(model, new ComponentMerger(model), new DeploymentMerger(model),
                new ComponentAssemblyRelationMerger(model), new ComponentAllocationRelationMerger(model));
    }

    public PcmOrchestrator() {
        this(new PcmSurrogate());
    }
}
