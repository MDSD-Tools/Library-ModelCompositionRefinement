package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.RelationMergerTest;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Interface;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.ServiceEffectSpecification;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Signature;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceProvisionRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.SignatureProvisionRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.SignatureSpecificationRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.pseudo.ComponentSignatureProvisionRelation;
import com.gstuer.modelmerging.test.utility.ElementFactory;

public class SignatureSpecificationRelationMergerTest extends RelationMergerTest<SignatureSpecificationRelationMerger,
        PcmSurrogate, SignatureSpecificationRelation, ComponentSignatureProvisionRelation, ServiceEffectSpecification> {
    @Override
    protected SignatureSpecificationRelation createRelation(ComponentSignatureProvisionRelation source,
            ServiceEffectSpecification destination,
            boolean isPlaceholder) {
        return new SignatureSpecificationRelation(source, destination, isPlaceholder);
    }

    @Override
    protected ComponentSignatureProvisionRelation getUniqueNonPlaceholderSourceEntity() {
        Component component = Component.getUniquePlaceholder();
        Interface interfsc = Interface.getUniquePlaceholder();
        Signature signature = Signature.getUniquePlaceholder();
        InterfaceProvisionRelation interfaceProvision = new InterfaceProvisionRelation(component,
                interfsc, true);
        SignatureProvisionRelation signatureProvision = new SignatureProvisionRelation(signature,
                interfsc, true);
        return new ComponentSignatureProvisionRelation(interfaceProvision, signatureProvision, false);

    }

    @Override
    protected ComponentSignatureProvisionRelation getPlaceholderOfSourceEntity(
            ComponentSignatureProvisionRelation source) {
        return new ComponentSignatureProvisionRelation(source.getSource(), source.getDestination(), true);
    }

    @Override
    protected ServiceEffectSpecification getUniqueNonPlaceholderDestinationEntity() {
        return ElementFactory.createUniqueServiceEffectSpecification(false);
    }

    @Override
    protected ServiceEffectSpecification getPlaceholderOfDestinationEntity(ServiceEffectSpecification destination) {
        return new ServiceEffectSpecification(destination.getValue(), true);
    }

    @Override
    protected SignatureSpecificationRelationMerger createMerger(PcmSurrogate model) {
        return new SignatureSpecificationRelationMerger(model);
    }

    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }
}
