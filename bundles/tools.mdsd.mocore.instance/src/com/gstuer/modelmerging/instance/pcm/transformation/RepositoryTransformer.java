package com.gstuer.modelmerging.instance.pcm.transformation;

import java.util.List;

import org.palladiosimulator.generator.fluent.repository.api.Repo;
import org.palladiosimulator.generator.fluent.repository.factory.FluentRepositoryFactory;
import org.palladiosimulator.generator.fluent.repository.structure.components.BasicComponentCreator;
import org.palladiosimulator.generator.fluent.repository.structure.interfaces.OperationInterfaceCreator;
import org.palladiosimulator.generator.fluent.repository.structure.interfaces.OperationSignatureCreator;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Parameter;
import org.palladiosimulator.pcm.repository.Repository;

import com.gstuer.modelmerging.framework.transformation.Transformer;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Interface;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Signature;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceProvisionRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceRequirementRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.SignatureProvisionRelation;

public class RepositoryTransformer implements Transformer<PcmSurrogate, Repository> {
    private static final String ROLE_PROVIDES_NAME_PATTERN = "%s Provider";
    private static final String ROLE_REQUIRES_NAME_PATTERN = "%s Consumer";

    @Override
    public Repository transform(PcmSurrogate model) {
        FluentRepositoryFactory repositoryFactory = new FluentRepositoryFactory();
        Repo fluentRepository = repositoryFactory.newRepository();

        List<InterfaceProvisionRelation> provisionRelations = model.getByType(InterfaceProvisionRelation.class);
        List<InterfaceRequirementRelation> requirementRelations = model.getByType(InterfaceRequirementRelation.class);
        List<SignatureProvisionRelation> signatureRelations = model.getByType(SignatureProvisionRelation.class);
        List<Interface> interfaces = model.getByType(Interface.class);

        // Add interfaces to fluent repository
        for (Interface interfaceInstance : interfaces) {
            OperationInterfaceCreator interfaceCreator = getCreator(repositoryFactory, interfaceInstance);

            // Add signatures
            for (SignatureProvisionRelation relation : signatureRelations) {
                if (relation.getDestination().equals(interfaceInstance)) {
                    Signature signature = relation.getSource();
                    OperationSignatureCreator signatureCreator = getCreator(repositoryFactory, signature);
                    interfaceCreator.withOperationSignature(signatureCreator);
                }
            }

            fluentRepository.addToRepository(interfaceCreator);
        }

        // Add components to fluent repository
        for (Component component : model.getByType(Component.class)) {
            BasicComponentCreator componentCreator = getCreator(repositoryFactory, component);

            // Add provided interfaces
            for (InterfaceProvisionRelation relation : provisionRelations) {
                Interface interfaceInstance = relation.getDestination();
                if (relation.getSource().equals(component)) {
                    String interfaceName = interfaceInstance.getValue().getEntityName();
                    OperationInterface operationInterface = repositoryFactory.fetchOfOperationInterface(interfaceName);
                    componentCreator.provides(operationInterface, getProvidedRoleName(interfaceInstance));
                }
            }

            // Add required interfaces
            for (InterfaceRequirementRelation relation : requirementRelations) {
                Interface interfaceInstance = relation.getDestination();
                if (relation.getSource().equals(component)) {
                    String interfaceName = interfaceInstance.getValue().getEntityName();
                    OperationInterface operationInterface = repositoryFactory.fetchOfOperationInterface(interfaceName);
                    componentCreator.requires(operationInterface, getRequiredRoleName(interfaceInstance));
                }
            }

            fluentRepository.addToRepository(componentCreator);
        }

        return fluentRepository.createRepositoryNow();
    }

    private BasicComponentCreator getCreator(FluentRepositoryFactory fluentFactory, Component component) {
        BasicComponentCreator componentCreator = fluentFactory.newBasicComponent();

        // TODO Identify important information within wrapped component
        // Copy information from wrapped component, dismiss deprecated information.
        BasicComponent wrappedComponent = component.getValue();
        componentCreator.withName(wrappedComponent.getEntityName());

        return componentCreator;
    }

    private OperationInterfaceCreator getCreator(FluentRepositoryFactory fluentFactory, Interface interfaceInstance) {
        OperationInterfaceCreator interfaceCreator = fluentFactory.newOperationInterface();

        // TODO Identify important information within wrapped interface
        // Copy information from wrapped interface, dismiss deprecated information.
        OperationInterface wrappedInterface = interfaceInstance.getValue();
        interfaceCreator.withName(wrappedInterface.getEntityName());

        return interfaceCreator;
    }

    private OperationSignatureCreator getCreator(FluentRepositoryFactory fluentFactory, Signature signature) {
        OperationSignatureCreator signatureCreator = fluentFactory.newOperationSignature();

        // Copy information from wrapped signature, dismiss deprecated information.
        OperationSignature wrappedSignature = signature.getValue();
        signatureCreator.withName(wrappedSignature.getEntityName());
        signatureCreator.withReturnType(wrappedSignature.getReturnType__OperationSignature());
        for (Parameter parameter : wrappedSignature.getParameters__OperationSignature()) {
            signatureCreator.withParameter(parameter.getParameterName(), parameter.getDataType__Parameter(),
                    parameter.getModifier__Parameter());
        }

        return signatureCreator;
    }

    protected static String getProvidedRoleName(Interface interfaceInstance) {
        String interfaceEntityName = interfaceInstance.getValue().getEntityName();
        return String.format(ROLE_PROVIDES_NAME_PATTERN, interfaceEntityName);
    }

    protected static String getRequiredRoleName(Interface interfaceInstance) {
        String interfaceEntityName = interfaceInstance.getValue().getEntityName();
        return String.format(ROLE_REQUIRES_NAME_PATTERN, interfaceEntityName);
    }
}
