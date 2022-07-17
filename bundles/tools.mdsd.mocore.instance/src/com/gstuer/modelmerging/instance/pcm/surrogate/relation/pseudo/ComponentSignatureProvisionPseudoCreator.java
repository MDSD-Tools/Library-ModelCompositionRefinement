package com.gstuer.modelmerging.instance.pcm.surrogate.relation.pseudo;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.gstuer.modelmerging.framework.surrogate.Model;
import com.gstuer.modelmerging.framework.surrogate.PseudoReplaceableCreator;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceProvisionRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.SignatureProvisionRelation;

public class ComponentSignatureProvisionPseudoCreator
        extends PseudoReplaceableCreator<ComponentSignatureProvisionRelation> {
    public ComponentSignatureProvisionPseudoCreator() {
        super(ComponentSignatureProvisionRelation.class);

    }

    @Override
    public List<ComponentSignatureProvisionRelation> getPseudoReplaceables(Model model) {
        List<InterfaceProvisionRelation> interfaceProvisions = model.getByType(InterfaceProvisionRelation.class);
        List<SignatureProvisionRelation> signatureProvisions = model.getByType(SignatureProvisionRelation.class);

        Set<ComponentSignatureProvisionRelation> pseudoReplaceables = new HashSet<>();
        for (InterfaceProvisionRelation interfaceProvision : interfaceProvisions) {
            for (SignatureProvisionRelation signatureProvision : signatureProvisions) {
                if (interfaceProvision.getDestination().equals(signatureProvision.getDestination())) {
                    boolean isPlaceholder = interfaceProvision.isPlaceholder() || signatureProvision.isPlaceholder();
                    ComponentSignatureProvisionRelation pseudoRelation = new ComponentSignatureProvisionRelation(
                            interfaceProvision, signatureProvision, isPlaceholder);
                    pseudoReplaceables.add(pseudoRelation);
                }
            }
        }

        return new LinkedList<>(pseudoReplaceables);
    }
}
