/* Copyright (C) 2023 Moritz Gstuer - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the EUPL 1.2 license.
 *
 * You should have received a copy of the EUPL 1.2 license
 * with this file. If not, please visit:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */
package tools.mdsd.mocore.framework.orchestration;

public class UnavailableProcessorException extends IllegalArgumentException {
    private static final long serialVersionUID = 7358182453030322135L;
    private static final String MESSAGE_PATTERN = "No processor available for discovery of type %s.";

    public UnavailableProcessorException(Class<?> discoveryType) {
        super(String.format(MESSAGE_PATTERN, discoveryType.getName()));
    }
}
