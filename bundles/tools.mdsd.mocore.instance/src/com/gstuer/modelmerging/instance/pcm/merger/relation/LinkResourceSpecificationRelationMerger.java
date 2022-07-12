package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.RelationMerger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.LinkResourceSpecificationRelation;

public class LinkResourceSpecificationRelationMerger
        extends RelationMerger<PcmSurrogate, LinkResourceSpecificationRelation> {
    public LinkResourceSpecificationRelationMerger(PcmSurrogate model) {
        super(model, LinkResourceSpecificationRelation.class);
    }
}
