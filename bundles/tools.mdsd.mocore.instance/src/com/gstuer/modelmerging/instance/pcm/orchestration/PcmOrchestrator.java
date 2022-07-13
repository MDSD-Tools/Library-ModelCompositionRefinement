package com.gstuer.modelmerging.instance.pcm.orchestration;

import com.gstuer.modelmerging.framework.orchestration.Orchestrator;
import com.gstuer.modelmerging.instance.pcm.merger.element.ComponentMerger;
import com.gstuer.modelmerging.instance.pcm.merger.element.DeploymentMerger;
import com.gstuer.modelmerging.instance.pcm.merger.element.InterfaceMerger;
import com.gstuer.modelmerging.instance.pcm.merger.element.LinkResourceSpecificationMerger;
import com.gstuer.modelmerging.instance.pcm.merger.element.SignatureMerger;
import com.gstuer.modelmerging.instance.pcm.merger.relation.ComponentAllocationRelationMerger;
import com.gstuer.modelmerging.instance.pcm.merger.relation.ComponentAssemblyRelationMerger;
import com.gstuer.modelmerging.instance.pcm.merger.relation.DeploymentDeploymentRelationMerger;
import com.gstuer.modelmerging.instance.pcm.merger.relation.InterfaceProvisionRelationMerger;
import com.gstuer.modelmerging.instance.pcm.merger.relation.InterfaceRequirementRelationMerger;
import com.gstuer.modelmerging.instance.pcm.merger.relation.LinkResourceSpecificationRelationMerger;
import com.gstuer.modelmerging.instance.pcm.merger.relation.SignatureProvisionRelationMerger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;

public class PcmOrchestrator extends Orchestrator<PcmSurrogate> {
    public PcmOrchestrator(PcmSurrogate model) {
        super(model, new SignatureMerger(model), new InterfaceMerger(model), new ComponentMerger(model),
                new DeploymentMerger(model), new LinkResourceSpecificationMerger(model),
                new SignatureProvisionRelationMerger(model), new InterfaceProvisionRelationMerger(model),
                new InterfaceRequirementRelationMerger(model), new ComponentAssemblyRelationMerger(model),
                new ComponentAllocationRelationMerger(model), new DeploymentDeploymentRelationMerger(model),
                new LinkResourceSpecificationRelationMerger(model));
    }

    public PcmOrchestrator() {
        this(new PcmSurrogate());
    }
}
