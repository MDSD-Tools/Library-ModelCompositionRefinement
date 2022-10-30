package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.RelationMergerTest;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Interface;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.ServiceEffectSpecification;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Signature;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentSignatureProvisionRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceProvisionRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.SignatureProvisionRelation;
import com.gstuer.modelmerging.instance.pcm.utility.ElementFactory;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ServiceEffectSpecificationRelation;

public class ServiceEffectSpecificationRelationMergerTest extends RelationMergerTest<ServiceEffectSpecificationRelationMerger,
        PcmSurrogate, ServiceEffectSpecificationRelation, ComponentSignatureProvisionRelation, ServiceEffectSpecification> {
    @Override
    protected ServiceEffectSpecificationRelation createRelation(ComponentSignatureProvisionRelation source,
            ServiceEffectSpecification destination,
            boolean isPlaceholder) {
        return new ServiceEffectSpecificationRelation(source, destination, isPlaceholder);
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
    protected ServiceEffectSpecificationRelationMerger createMerger(PcmSurrogate model) {
        return new ServiceEffectSpecificationRelationMerger(model);
    }

    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }
}
