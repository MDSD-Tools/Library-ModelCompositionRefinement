package com.gstuer.modelmerging.instance.pcm.surrogate;

import com.gstuer.modelmerging.framework.surrogate.ElementTest;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;

public class DeploymentTest extends ElementTest<Deployment, String> {
    @Override
    protected Deployment createElement(String value, boolean isPlaceholder) {
        return new Deployment(value, isPlaceholder);
    }

    @Override
    protected String getUniqueValue() {
        return String.valueOf(getUniqueLongValue());
    }

    @Override
    protected Deployment getUniqueNonPlaceholder() {
        return new Deployment(getUniqueValue(), false);
    }

    @Override
    protected Deployment getPlaceholderOf(Deployment replaceable) {
        return new Deployment(replaceable.getValue(), true);
    }
}
