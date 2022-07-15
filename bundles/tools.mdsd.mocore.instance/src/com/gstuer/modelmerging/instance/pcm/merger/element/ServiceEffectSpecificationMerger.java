package com.gstuer.modelmerging.instance.pcm.merger.element;

import java.util.List;

import com.gstuer.modelmerging.framework.merger.Merger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.ServiceEffectSpecification;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Signature;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.SignatureSpecificationRelation;

public class ServiceEffectSpecificationMerger extends Merger<PcmSurrogate, ServiceEffectSpecification> {
    public ServiceEffectSpecificationMerger(PcmSurrogate model) {
        super(model, ServiceEffectSpecification.class);
    }

    @Override
    protected void refine(ServiceEffectSpecification discovery) {
        List<SignatureSpecificationRelation> signatureSpecifications = getModel()
                .getByType(SignatureSpecificationRelation.class);
        signatureSpecifications.removeIf(relation -> !relation.getDestination().equals(discovery));

        if (signatureSpecifications.isEmpty()) {
            Signature signature = Signature.getUniquePlaceholder();
            SignatureSpecificationRelation relation = new SignatureSpecificationRelation(signature, discovery, true);
            addImplication(relation);
        }
    }
}
