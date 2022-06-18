package com.gstuer.modelmerging.instance.pcm.surrogate.element;

import com.gstuer.modelmerging.framework.surrogate.ElementTest;

public class ComponentTest extends ElementTest<Component, String> {
    @Override
    protected Component createElement(String value, boolean isPlaceholder) {
        return new Component(value, isPlaceholder);
    }

    @Override
    protected String getUniqueValue() {
        return String.valueOf(getUniqueLongValue());
    }

    @Override
    protected Component getUniqueNonPlaceholder() {
        return new Component(getUniqueValue(), false);
    }

    @Override
    protected Component getPlaceholderOf(Component replaceable) {
        return new Component(replaceable.getValue(), true);
    }
}
