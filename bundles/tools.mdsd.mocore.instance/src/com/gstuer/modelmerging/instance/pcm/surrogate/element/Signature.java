package com.gstuer.modelmerging.instance.pcm.surrogate.element;

import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.RepositoryFactory;

import com.gstuer.modelmerging.framework.surrogate.Element;

public class Signature extends Element<OperationSignature> {
    public Signature(OperationSignature value, boolean isPlaceholder) {
        super(value, isPlaceholder);
    }

    public static Signature getUniquePlaceholder() {
        String identifier = "Placeholder_" + getUniqueValue();
        OperationSignature value = RepositoryFactory.eINSTANCE.createOperationSignature();
        value.setEntityName(identifier);
        return new Signature(value, true);
    }
}
