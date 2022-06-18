package com.gstuer.modelmerging.instance.pcm.surrogate;

import com.gstuer.modelmerging.framework.surrogate.ElementTest;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Interface;

public class InterfaceTest extends ElementTest<Interface, String> {
    @Override
    protected Interface createElement(String value, boolean isPlaceholder) {
        return new Interface(value, isPlaceholder);
    }

    @Override
    protected String getUniqueValue() {
        return String.valueOf(getUniqueLongValue());
    }

    @Override
    protected Interface getUniqueNonPlaceholder() {
        return new Interface(getUniqueValue(), false);
    }

    @Override
    protected Interface getPlaceholderOf(Interface replaceable) {
        return new Interface(replaceable.getValue(), true);
    }
}
