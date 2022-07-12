package com.gstuer.modelmerging.instance.pcm.merger.relation;

import java.util.List;
import java.util.Objects;

import com.gstuer.modelmerging.framework.merger.RelationMerger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.LinkResourceSpecification;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.DeploymentDeploymentRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.LinkResourceSpecificationRelation;

public class DeploymentDeploymentRelationMerger extends RelationMerger<PcmSurrogate, DeploymentDeploymentRelation> {
    public DeploymentDeploymentRelationMerger(PcmSurrogate model) {
        super(model, DeploymentDeploymentRelation.class);
    }

    @Override
    protected void refine(DeploymentDeploymentRelation discovery) {
        super.refine(discovery);

        // Check whether a linking resource specification already exists for the link to be merged
        List<LinkResourceSpecificationRelation> specifications = this.getModel()
                .getByType(LinkResourceSpecificationRelation.class);
        specifications.removeIf(specification -> !Objects.equals(discovery, specification.getDestination()));
        if (specifications.isEmpty()) {
            // Add implicit placeholder specification, if no specification is found for this deployment link
            LinkResourceSpecification placeholderSpecification = LinkResourceSpecification.getUniquePlaceholder();
            LinkResourceSpecificationRelation implicitRelation = new LinkResourceSpecificationRelation(
                    placeholderSpecification, discovery, true);
            this.addImplication(implicitRelation);
        }
    }
}
