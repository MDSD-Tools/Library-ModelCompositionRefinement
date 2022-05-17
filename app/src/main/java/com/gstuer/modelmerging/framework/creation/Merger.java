package com.gstuer.modelmerging.framework.creation;

import com.gstuer.modelmerging.framework.surrogate.Model;

public interface Merger<M extends Model, T> {
    M merge(M model, T discovery);
}
