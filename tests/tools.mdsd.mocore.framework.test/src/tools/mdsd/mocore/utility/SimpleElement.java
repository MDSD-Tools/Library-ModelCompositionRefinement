/* Copyright (C) 2023 Moritz Gstuer - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the EUPL 1.2 license.
 *
 * You should have received a copy of the EUPL 1.2 license
 * with this file. If not, please visit:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */
package tools.mdsd.mocore.utility;

import tools.mdsd.mocore.framework.surrogate.Element;

public class SimpleElement extends Element<Long> {
    private static long nextIdentifier;

    public SimpleElement(boolean isPlaceholder) {
        super(nextIdentifier++, isPlaceholder);
    }
}
