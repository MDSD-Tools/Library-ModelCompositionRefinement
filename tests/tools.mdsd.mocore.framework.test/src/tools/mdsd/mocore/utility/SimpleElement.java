package tools.mdsd.mocore.utility;

import tools.mdsd.mocore.framework.surrogate.Element;

public class SimpleElement extends Element<Long> {
    private static long nextIdentifier;

    public SimpleElement(boolean isPlaceholder) {
        super(nextIdentifier++, isPlaceholder);
    }
}
