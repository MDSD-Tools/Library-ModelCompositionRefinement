package com.gstuer.modelmerging.instance.pcm.merger.element;

import java.util.List;

import com.gstuer.modelmerging.framework.merger.Merger;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Interface;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Signature;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.SignatureInterfaceRelation;

public class SignatureMerger extends Merger<PcmSurrogate, Signature> {
    public SignatureMerger(PcmSurrogate model) {
        super(model, Signature.class);
    }

    @Override
    protected void refine(Signature discovery) {
        List<SignatureInterfaceRelation> interfaceRelations = getModel().getByType(SignatureInterfaceRelation.class);
        interfaceRelations.removeIf(relation -> !relation.getSource().equals(discovery));
        
        if (interfaceRelations.isEmpty()) {
            // TODO Replace with concrete pcm placeholder
            Interface interfaceElement = new Interface("Placeholder", true);
            SignatureInterfaceRelation relation = new SignatureInterfaceRelation(discovery, interfaceElement, true);
            addImplication(relation);
        }
    }

}
