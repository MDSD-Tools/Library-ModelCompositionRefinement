package com.gstuer.modelmerging.framework.orchestration;

public class UnavailableMergerException extends IllegalArgumentException {
    private static final long serialVersionUID = 7358182453030322135L;
    private static final String MESSAGE_PATTERN = "No merger available for discovery of type %s.";

    @Deprecated
    public UnavailableMergerException(String message) {
        super(message);
    }

    public UnavailableMergerException(Class<?> discoveryType) {
        super(String.format(MESSAGE_PATTERN, discoveryType.getName()));
    }
}
