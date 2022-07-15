package com.gstuer.modelmerging.instance.pcm.surrogate.relation;

import com.gstuer.modelmerging.framework.surrogate.Relation;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.ServiceEffectSpecification;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Signature;

public class SignatureSpecificationRelation extends Relation<Signature, ServiceEffectSpecification> {
    public SignatureSpecificationRelation(Signature source, ServiceEffectSpecification destination,
            boolean isPlaceholder) {
        super(source, destination, isPlaceholder);
    }

    @Override
    public <U extends Replaceable> SignatureSpecificationRelation replace(U original, U replacement) {
        if (!this.canReplace(original)) {
            // TODO Add message to exception
            throw new IllegalArgumentException();
        }
        if (this.equals(original)) {
            return (SignatureSpecificationRelation) replacement;
        }
        Signature source = getSourceReplacement(original, replacement);
        ServiceEffectSpecification destination = getDestinationReplacement(original, replacement);
        return new SignatureSpecificationRelation(source, destination, this.isPlaceholder());
    }
}
