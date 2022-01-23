package com.gstuer.modelmerging.creation;

public interface Merger<M extends Model, T> {
    M merge(M model, T discovery);
}
