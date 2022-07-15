package com.gstuer.modelmerging.instance.pcm.merger.relation;

import com.gstuer.modelmerging.framework.merger.RelationMergerTest;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.ServiceEffectSpecification;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Signature;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.SignatureSpecificationRelation;
import com.gstuer.modelmerging.test.utility.ElementFactory;

public class SignatureSpecificationRelationMergerTest extends RelationMergerTest<SignatureSpecificationRelationMerger,
        PcmSurrogate, SignatureSpecificationRelation, Signature, ServiceEffectSpecification> {
    @Override
    protected SignatureSpecificationRelation createRelation(Signature source, ServiceEffectSpecification destination,
            boolean isPlaceholder) {
        return new SignatureSpecificationRelation(source, destination, isPlaceholder);
    }

    @Override
    protected Signature getUniqueNonPlaceholderSourceEntity() {
        return ElementFactory.createUniqueSignature(false);
    }

    @Override
    protected Signature getPlaceholderOfSourceEntity(Signature source) {
        return new Signature(source.getValue(), true);
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
