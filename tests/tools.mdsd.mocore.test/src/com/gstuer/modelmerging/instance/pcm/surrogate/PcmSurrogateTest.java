package com.gstuer.modelmerging.instance.pcm.surrogate;

import com.gstuer.modelmerging.framework.surrogate.ModelTest;
import com.gstuer.modelmerging.framework.surrogate.Relation;
import com.gstuer.modelmerging.test.utility.SimpleElement;
import com.gstuer.modelmerging.test.utility.SimpleRelation;

public class PcmSurrogateTest extends ModelTest<PcmSurrogate, SimpleElement> {
    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }

    @Override
    protected SimpleElement createUniqueReplaceable() {
        return new SimpleElement(false);
    }

    @Override
    protected Relation<SimpleElement, SimpleElement> createRelation(SimpleElement source, SimpleElement destination,
            boolean isPlaceholder) {
        return new SimpleRelation(source, destination, isPlaceholder);
    }
}
