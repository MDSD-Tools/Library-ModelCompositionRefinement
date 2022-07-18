package com.gstuer.modelmerging.instance.pcm.transformation;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.palladiosimulator.generator.fluent.repository.api.Repo;
import org.palladiosimulator.generator.fluent.repository.factory.FluentRepositoryFactory;
import org.palladiosimulator.generator.fluent.repository.structure.components.BasicComponentCreator;
import org.palladiosimulator.generator.fluent.repository.structure.interfaces.OperationInterfaceCreator;
import org.palladiosimulator.generator.fluent.repository.structure.interfaces.OperationSignatureCreator;
import org.palladiosimulator.pcm.reliability.FailureType;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.ExceptionType;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Parameter;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.seff.ServiceEffectSpecification;

import com.gstuer.modelmerging.framework.transformation.Transformer;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Interface;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Signature;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceProvisionRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceRequirementRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ServiceEffectSpecificationRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.SignatureProvisionRelation;

public class RepositoryTransformer implements Transformer<PcmSurrogate, Repository> {
    private static final String ROLE_PROVIDES_NAME_PATTERN = "%s Provider";
    private static final String ROLE_REQUIRES_NAME_PATTERN = "%s Consumer";

    @Override
    public Repository transform(PcmSurrogate model) {
        FluentRepositoryFactory repositoryFactory = new FluentRepositoryFactory();
        Repo fluentRepository = repositoryFactory.newRepository();

        List<ServiceEffectSpecificationRelation> seffRelations = model
                .getByType(ServiceEffectSpecificationRelation.class);
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

            // Build component to make changes that are unsupported by fluent api
            BasicComponent repositoryComponent = componentCreator.build();

            // Add service effect specifications to component
            // For each provided interface, iterate over each signature of interface and add seff if it exists
            Stream<ServiceEffectSpecificationRelation> seffRelationStream = seffRelations.stream()
                    .filter(relation -> relation.getSource().getSource().getSource().equals(component));
            for (InterfaceProvisionRelation interfaceProvision : provisionRelations) {
                if (interfaceProvision.getSource().equals(component)) {
                    OperationInterface operationInterface = repositoryFactory
                            .fetchOfOperationInterface(interfaceProvision.getDestination().getValue().getEntityName());
                    for (OperationSignature signature : operationInterface.getSignatures__OperationInterface()) {
                        // Get seff entity for specific signature in interface
                        Predicate<ServiceEffectSpecificationRelation> filter = relation -> {
                            final Signature wrappedSignature = relation.getSource().getDestination().getSource();
                            final Interface wrappedInterface = relation.getSource().getSource().getDestination();
                            return representSameSignature(signature, wrappedSignature.getValue()) &&
                                    representSameInterface(operationInterface, wrappedInterface.getValue());
                        };
                        ServiceEffectSpecification seff = seffRelationStream
                                .filter(filter)
                                .map(relation -> relation.getDestination().getValue()).findFirst()
                                .orElse(com.gstuer.modelmerging.instance.pcm.surrogate.element.ServiceEffectSpecification
                                        .getUniquePlaceholder().getValue());

                        // Reset component and signature within seff because they may be out-dated
                        seff.setBasicComponent_ServiceEffectSpecification(repositoryComponent);
                        seff.setDescribedService__SEFF(signature);
                    }
                }
            }

            fluentRepository.addToRepository(repositoryComponent);
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
        for (ExceptionType exceptionType : wrappedSignature.getExceptions__Signature()) {
            signatureCreator.withExceptionType(exceptionType);
        }
        for (FailureType failureType : wrappedSignature.getFailureType()) {
            signatureCreator.withFailureType(failureType);
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

    // TODO Test and move to evaluation helper
    private static boolean representSameSignature(OperationSignature signature, OperationSignature otherSignature) {
        boolean equalName = Objects.equals(signature.getEntityName(), otherSignature.getEntityName());
        boolean equalReturn = Objects.equals(signature.getReturnType__OperationSignature(),
                otherSignature.getReturnType__OperationSignature());
        boolean equalParameters = signature.getParameters__OperationSignature()
                .containsAll(otherSignature.getParameters__OperationSignature())
                && otherSignature.getParameters__OperationSignature()
                        .containsAll(signature.getParameters__OperationSignature());
        return equalName && equalReturn && equalParameters;
    }

    // TODO Test and move to evaluation helper
    private static boolean representSameInterface(OperationInterface interFace, OperationInterface otherInterFace) {
        boolean equalName = Objects.equals(interFace.getEntityName(), otherInterFace.getEntityName());
        // TODO Check if signatures are equal via representSameSignature
        return equalName;
    }
}
