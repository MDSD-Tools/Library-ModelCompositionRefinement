package com.gstuer.modelmerging.instance.pcm.surrogate.relation;

import com.gstuer.modelmerging.framework.surrogate.Relation;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Interface;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Signature;

public class SignatureInterfaceRelation extends Relation<Signature, Interface> {
    public SignatureInterfaceRelation(Signature source, Interface destination, boolean isPlaceholder) {
        super(source, destination, isPlaceholder);
    }
}
