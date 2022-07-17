package com.gstuer.modelmerging.instance.pcm.merger.element;

import java.util.List;

import com.gstuer.modelmerging.framework.merger.Merger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Interface;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Signature;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.SignatureProvisionRelation;

public class SignatureMerger extends Merger<PcmSurrogate, Signature> {
    public SignatureMerger(PcmSurrogate model) {
        super(model, Signature.class);
    }

    @Override
    protected void refine(Signature discovery) {
        // Add providing interface for signature if none exists
        List<SignatureProvisionRelation> interfaceRelations = getModel().getByType(SignatureProvisionRelation.class);
        interfaceRelations.removeIf(relation -> !relation.getSource().equals(discovery));
        if (interfaceRelations.isEmpty()) {
            Interface interfaceElement = Interface.getUniquePlaceholder();
            SignatureProvisionRelation relation = new SignatureProvisionRelation(discovery, interfaceElement, true);
            addImplication(relation);
        }
    }
}
