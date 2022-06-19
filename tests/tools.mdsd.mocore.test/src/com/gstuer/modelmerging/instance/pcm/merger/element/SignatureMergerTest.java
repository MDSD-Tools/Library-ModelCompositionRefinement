package com.gstuer.modelmerging.instance.pcm.merger.element;

import com.gstuer.modelmerging.framework.merger.MergerTest;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Signature;

public class SignatureMergerTest extends MergerTest<SignatureMerger, PcmSurrogate, Signature> {
    @Override
    protected SignatureMerger createMerger(PcmSurrogate model) {
        return new SignatureMerger(model);
    }

    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }

    @Override
    protected Signature createUniqueReplaceable() {
        return Signature.getUniquePlaceholder();
    }
}
