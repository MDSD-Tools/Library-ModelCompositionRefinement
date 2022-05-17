package com.gstuer.modelmerging.framework.transformation;

import com.gstuer.modelmerging.framework.surrogate.Model;

public interface Transformer<M extends Model, N> {
    N transform(M model);
}
