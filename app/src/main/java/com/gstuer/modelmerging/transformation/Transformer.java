package com.gstuer.modelmerging.transformation;

import com.gstuer.modelmerging.creation.Model;

public interface Transformer<M extends Model, N> {
    N transform(M model);
}
