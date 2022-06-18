package com.gstuer.modelmerging.instance.pcm.surrogate;

import com.gstuer.modelmerging.framework.surrogate.ElementTest;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Signature;

public class SignatureTest extends ElementTest<Signature, String> {
    @Override
    protected Signature createElement(String value, boolean isPlaceholder) {
        return new Signature(value, isPlaceholder);
    }

    @Override
    protected String getUniqueValue() {
        return String.valueOf(getUniqueLongValue());
    }

    @Override
    protected Signature getUniqueNonPlaceholder() {
        return new Signature(getUniqueValue(), false);
    }

    @Override
    protected Signature getPlaceholderOf(Signature replaceable) {
        return new Signature(replaceable.getValue(), true);
    }
}
