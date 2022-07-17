package com.gstuer.modelmerging.instance.pcm.surrogate.relation.pseudo;

import java.util.Objects;

import com.gstuer.modelmerging.framework.surrogate.Relation;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceProvisionRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.SignatureProvisionRelation;

public class ComponentSignatureProvisionRelation extends Relation<InterfaceProvisionRelation, SignatureProvisionRelation> {
    private static final String ERROR_UNEQUAL_INTERFACE = "Interfaces of relations have to be equal.";

    public ComponentSignatureProvisionRelation(InterfaceProvisionRelation source, SignatureProvisionRelation destination,
            boolean isPlaceholder) {
        super(source, destination, isPlaceholder);
        if (!Objects.equals(source.getDestination(), destination.getDestination())) {
            throw new IllegalArgumentException(ERROR_UNEQUAL_INTERFACE);
        }
    }

    @Override
    public <U extends Replaceable> ComponentSignatureProvisionRelation replace(U original, U replacement) {
        if (!this.canReplace(original)) {
            // TODO Add message to exception
            throw new IllegalArgumentException();
        }
        if (this.equals(original)) {
            return (ComponentSignatureProvisionRelation) replacement;
        }
        InterfaceProvisionRelation source = getSourceReplacement(original, replacement);
        SignatureProvisionRelation destination = getDestinationReplacement(original, replacement);
        return new ComponentSignatureProvisionRelation(source, destination, this.isPlaceholder());
    }
}
