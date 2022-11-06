package com.gstuer.modelmerging.framework.orchestration;

public class UnavailableProcessorException extends IllegalArgumentException {
    private static final long serialVersionUID = 7358182453030322135L;
    private static final String MESSAGE_PATTERN = "No processor available for discovery of type %s.";

    public UnavailableProcessorException(Class<?> discoveryType) {
        super(String.format(MESSAGE_PATTERN, discoveryType.getName()));
    }
}
