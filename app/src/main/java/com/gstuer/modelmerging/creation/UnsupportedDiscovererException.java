package com.gstuer.modelmerging.creation;

import java.io.Serial;

public class UnsupportedDiscovererException extends IllegalArgumentException {
    @Serial
    private static final long serialVersionUID = 7358182453030322135L;

    public UnsupportedDiscovererException(String message) {
        super(message);
    }
}
