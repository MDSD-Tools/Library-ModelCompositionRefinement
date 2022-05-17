package com.gstuer.modelmerging.framework.creation;

public interface Merger<M extends Model, T> {
    M merge(M model, T discovery);
}
