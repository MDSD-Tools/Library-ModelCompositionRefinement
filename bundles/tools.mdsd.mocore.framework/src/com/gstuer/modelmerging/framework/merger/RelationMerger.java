package com.gstuer.modelmerging.framework.merger;

import com.gstuer.modelmerging.framework.surrogate.Model;
import com.gstuer.modelmerging.framework.surrogate.Relation;

public abstract class RelationMerger<M extends Model, T extends Relation<?, ?>> extends Merger<M, T> {
    protected RelationMerger(M model, Class<T> processableType) {
        super(model, processableType);
    }

    @Override
    public void process(T discovery) {
        super.process(discovery);

        // Add trivial implications for source and destination of relation
        addImplication(discovery.getSource());
        addImplication(discovery.getDestination());
    }
}
