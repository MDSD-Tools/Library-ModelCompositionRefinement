package com.gstuer.modelmerging.evaluation;

import static com.gstuer.modelmerging.evaluation.discovery.PcmDiscovererCreator.createComponentFromRepositoryComponent;
import static com.gstuer.modelmerging.test.utility.PcmEvaluationUtility.containsRepresentative;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.palladiosimulator.generator.fluent.shared.util.ModelLoader;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;
import org.palladiosimulator.pcm.system.System;

import com.google.common.io.Files;
import com.gstuer.modelmerging.evaluation.discovery.PcmDiscovererCreator;
import com.gstuer.modelmerging.framework.discovery.Discoverer;
import com.gstuer.modelmerging.framework.surrogate.Relation;
import com.gstuer.modelmerging.instance.pcm.orchestration.PcmOrchestrator;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Interface;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.LinkResourceSpecification;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.ServiceEffectSpecification;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Signature;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAllocationRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAssemblyRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentSignatureProvisionRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceProvisionRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceRequirementRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ServiceEffectSpecificationRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.SignatureProvisionRelation;
import com.gstuer.modelmerging.instance.pcm.transformation.AllocationTransformer;
import com.gstuer.modelmerging.instance.pcm.transformation.RepositoryTransformer;
import com.gstuer.modelmerging.instance.pcm.transformation.ResourceEnvironmentTransformer;
import com.gstuer.modelmerging.instance.pcm.transformation.SystemTransformer;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class CaseStudyTest {
    private static final boolean PRINT_INFOS = false;

    @BeforeAll
    public void setupTest() throws IOException {
        copyFiles();
        printInfos();
    }

    public void printInfos() {
        if (!PRINT_INFOS) {
            return;
        }
        EcorePlugin.ExtensionProcessor.process(null);
        Collection<Discoverer<?>> discoverers = getDiscoverers();
        Set<?> discoveries = discoverers.stream()
                .flatMap(disco -> disco.getDiscoveries().stream())
                .collect(Collectors.toSet());
        Set<?> containers = discoveries.stream().filter(element -> element instanceof Deployment)
                .collect(Collectors.toSet());
        Set<?> components = discoveries.stream().filter(element -> element instanceof Component)
                .collect(Collectors.toSet());
        Set<?> interfaces = discoveries.stream()
                .filter(element -> element instanceof Relation<?, ?>)
                .filter(element -> element instanceof InterfaceProvisionRelation
                        || element instanceof InterfaceRequirementRelation)
                .map(element -> ((Relation<?, ?>) element).getDestination())
                .collect(Collectors.toSet());
        Set<?> signatures = discoveries.stream()
                .filter(element -> element instanceof SignatureProvisionRelation)
                .map(element -> ((SignatureProvisionRelation) element).getSource())
                .collect(Collectors.toSet());
        Set<?> seffs = discoveries.stream()
                .filter(element -> element instanceof ServiceEffectSpecificationRelation)
                .map(element -> ((ServiceEffectSpecificationRelation) element).getDestination())
                .collect(Collectors.toSet());

        PcmOrchestrator orchestrator = new PcmOrchestrator();
        discoverers.forEach(orchestrator::processDiscoverer);
        PcmSurrogate model = orchestrator.getModel();
        Repository repository = new RepositoryTransformer().transform(model);
        ResourceEnvironment resourceEnvironment = new ResourceEnvironmentTransformer().transform(model);

        StringBuilder builder = new StringBuilder();
        builder.append("//// Case Study Test ////");
        builder.append(java.lang.System.lineSeparator());
        builder.append("Name:" + getCaseStudyName());
        builder.append(java.lang.System.lineSeparator());
        builder.append("Containers: " + containers.size() + "->"
                + resourceEnvironment.getResourceContainer_ResourceEnvironment().size());
        builder.append(java.lang.System.lineSeparator());
        builder.append("Components: " + components.size() + "->" + repository.getComponents__Repository().size());
        builder.append(java.lang.System.lineSeparator());
        builder.append("Interfaces: " + interfaces.size() + "->" + repository.getInterfaces__Repository().size());
        builder.append(java.lang.System.lineSeparator());
        builder.append("Signatures: " + signatures.size() + "->"
                + repository.getInterfaces__Repository().stream()
                        .filter(interFace -> interFace instanceof OperationInterface)
                        .mapToInt(interFace -> ((OperationInterface) interFace).getSignatures__OperationInterface()
                                .size())
                        .sum());
        builder.append(java.lang.System.lineSeparator());
        builder.append("Seffs: " + seffs.size() + "->"
                + repository.getComponents__Repository().stream()
                        .filter(component -> component instanceof BasicComponent)
                        .flatMap(component -> ((BasicComponent) component)
                                .getServiceEffectSpecifications__BasicComponent().stream())
                        .count());
        builder.append(java.lang.System.lineSeparator());
        java.lang.System.out.println(builder.toString());
    }

    public void copyFiles() throws IOException {
        File[] files = { getRepositoryFile(), getSystemFile(), getAllocationFile(), getResourceEnvironmentFile() };
        for (File file : files) {
            String fileName = file.getName();
            File rootFile = new File("./" + fileName);
            Files.copy(file, rootFile);
        }
    }

    @AfterAll
    public void deleteFiles() {
        File[] files = { getRepositoryFile(), getSystemFile(), getAllocationFile(), getResourceEnvironmentFile() };
        for (File file : files) {
            String fileName = file.getName();
            File rootFile = new File("./" + fileName);
            rootFile.delete();
        }
    }

    @BeforeEach
    public void resetExtensionProcessor() {
        EcorePlugin.ExtensionProcessor.process(null);
    }

    @Test
    public void testTransformedRepositoryContainsRelevantElements() {
        // Initialize orchestrator and get discoverers for case study models
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Collection<Discoverer<?>> discoverers = getDiscoverers();
        RepositoryTransformer transformer = new RepositoryTransformer();
        System system = loadSystem();

        // Process discoverers and transform output models
        discoverers.forEach(orchestrator::processDiscoverer);
        PcmSurrogate model = orchestrator.getModel();
        Repository repository = transformer.transform(model);

        // Assert all components are present
        for (AssemblyContext assemblyContext : system.getAssemblyContexts__ComposedStructure()) {
            RepositoryComponent originalComponent = assemblyContext.getEncapsulatedComponent__AssemblyContext();
            Component wrapper = createComponentFromRepositoryComponent(originalComponent);
            assertTrue(containsRepresentative(repository, wrapper));
        }

        // Assert all interfaces, interface provisions, interface requirements, & signatures are present
        for (Connector connector : system.getConnectors__ComposedStructure()) {
            if (connector instanceof AssemblyConnector) {
                AssemblyConnector assemblyConnector = (AssemblyConnector) connector;

                // Fetch source and destination of connector and check if they exist in repository
                Component provider = createComponentFromRepositoryComponent(assemblyConnector
                        .getProvidingAssemblyContext_AssemblyConnector().getEncapsulatedComponent__AssemblyContext());
                Component consumer = createComponentFromRepositoryComponent(assemblyConnector
                        .getRequiringAssemblyContext_AssemblyConnector().getEncapsulatedComponent__AssemblyContext());
                assertTrue(containsRepresentative(repository, provider));
                assertTrue(containsRepresentative(repository, consumer));

                // Fetch interface, provision, and requirement of connector and check if it exist in repository
                Interface interFace = new Interface(assemblyConnector.getProvidedRole_AssemblyConnector()
                        .getProvidedInterface__OperationProvidedRole(), false);
                InterfaceProvisionRelation interfaceProvision = new InterfaceProvisionRelation(provider,
                        interFace, false);
                InterfaceRequirementRelation interfaceRequirement = new InterfaceRequirementRelation(consumer,
                        interFace, false);
                assertTrue(containsRepresentative(repository, interFace));
                assertTrue(containsRepresentative(repository, interfaceProvision));
                assertTrue(containsRepresentative(repository, interfaceRequirement));

                // Fetch signatures of connector interface and check if they are provided correctly within repository
                List<Signature> signatures = interFace.getValue().getSignatures__OperationInterface().stream()
                        .map(operationSignature -> new Signature(operationSignature, false))
                        .toList();
                List<SignatureProvisionRelation> signatureProvisions = signatures.stream()
                        .map(signature -> new SignatureProvisionRelation(signature, interFace, false))
                        .toList();
                for (SignatureProvisionRelation provision : signatureProvisions) {
                    assertTrue(containsRepresentative(repository, provision));
                }

                // Fetch seffs of providing component and see if they exist in repository
                for (SignatureProvisionRelation signatureProvision : signatureProvisions) {
                    ComponentSignatureProvisionRelation componentSignatureProvision = new ComponentSignatureProvisionRelation(
                            interfaceProvision, signatureProvision, false);
                    List<ServiceEffectSpecification> seffs = provider.getValue()
                            .getServiceEffectSpecifications__BasicComponent().stream()
                            .filter(seff -> seff instanceof ResourceDemandingSEFF)
                            .map(seff -> (ResourceDemandingSEFF) seff)
                            .map(seff -> new ServiceEffectSpecification(seff, false))
                            .toList();
                    for (ServiceEffectSpecification seff : seffs) {
                        if (seff.getValue().getDescribedService__SEFF() instanceof OperationSignature) {
                            Signature otherSignature = new Signature(
                                    (OperationSignature) seff.getValue().getDescribedService__SEFF(), false);
                            if (signatureProvision.getSource().equals(otherSignature)) {
                                ServiceEffectSpecificationRelation seffRelation = new ServiceEffectSpecificationRelation(
                                        componentSignatureProvision, seff, false);
                                assertTrue(containsRepresentative(repository, seffRelation));
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testTransformedSystemContainsRelevantElements() {
        // Initialize orchestrator and get discoverers for case study models
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Collection<Discoverer<?>> discoverers = getDiscoverers();
        SystemTransformer transformer = new SystemTransformer();
        System originalSystem = loadSystem();

        // Process discoverers and transform output models
        discoverers.forEach(orchestrator::processDiscoverer);
        PcmSurrogate model = orchestrator.getModel();
        System system = transformer.transform(model);

        // Assert all assembly contexts are present
        for (AssemblyContext assemblyContext : originalSystem.getAssemblyContexts__ComposedStructure()) {
            RepositoryComponent originalComponent = assemblyContext.getEncapsulatedComponent__AssemblyContext();
            Component wrapper = createComponentFromRepositoryComponent(originalComponent);
            assertTrue(containsRepresentative(system, wrapper));
        }

        // Assert all assembly connectors are present
        for (Connector connector : originalSystem.getConnectors__ComposedStructure()) {
            if (connector instanceof AssemblyConnector) {
                AssemblyConnector assemblyConnector = (AssemblyConnector) connector;
                Component provider = createComponentFromRepositoryComponent(assemblyConnector
                        .getProvidingAssemblyContext_AssemblyConnector().getEncapsulatedComponent__AssemblyContext());
                Component consumer = createComponentFromRepositoryComponent(assemblyConnector
                        .getRequiringAssemblyContext_AssemblyConnector().getEncapsulatedComponent__AssemblyContext());
                Interface interFace = new Interface(assemblyConnector.getProvidedRole_AssemblyConnector()
                        .getProvidedInterface__OperationProvidedRole(), false);
                InterfaceProvisionRelation interfaceProvision = new InterfaceProvisionRelation(provider,
                        interFace, false);
                InterfaceRequirementRelation interfaceRequirement = new InterfaceRequirementRelation(consumer,
                        interFace, false);
                ComponentAssemblyRelation assemblyRelation = new ComponentAssemblyRelation(interfaceProvision,
                        interfaceRequirement, false);
                assertTrue(containsRepresentative(originalSystem, assemblyRelation));
            }
        }
    }

    @Test
    public void testTransformedAllocationContainsRelevantElements() {
        // Initialize orchestrator and get discoverers for case study models
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Collection<Discoverer<?>> discoverers = getDiscoverers();
        AllocationTransformer transformer = new AllocationTransformer();
        Allocation originalAllocation = loadAllocation();

        // Process discoverers and transform output models
        discoverers.forEach(orchestrator::processDiscoverer);
        PcmSurrogate model = orchestrator.getModel();
        Allocation allocation = transformer.transform(model);

        // Assert all assembly contexts are allocated correctly
        for (AllocationContext allocationContext : originalAllocation.getAllocationContexts_Allocation()) {
            Component component = createComponentFromRepositoryComponent(allocationContext
                    .getAssemblyContext_AllocationContext().getEncapsulatedComponent__AssemblyContext());
            Deployment deployment = new Deployment(allocationContext.getResourceContainer_AllocationContext(), false);
            ComponentAllocationRelation componentAllocation = new ComponentAllocationRelation(component,
                    deployment, false);
            assertTrue(containsRepresentative(allocation, componentAllocation));
        }
    }

    @Test
    public void testTransformedResourceEnvironmentContainsRelevantElements() {
        // Initialize orchestrator and get discoverers for case study models
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Collection<Discoverer<?>> discoverers = getDiscoverers();
        ResourceEnvironmentTransformer transformer = new ResourceEnvironmentTransformer();
        ResourceEnvironment originalEnvironment = loadResourceEnvironment();

        // Process discoverers and transform output models
        discoverers.forEach(orchestrator::processDiscoverer);
        PcmSurrogate model = orchestrator.getModel();
        ResourceEnvironment environment = transformer.transform(model);

        // Assert all containers are present
        originalEnvironment.getResourceContainer_ResourceEnvironment().forEach(originalContainer -> {
            final Deployment originalWrapper = new Deployment(originalContainer, false);
            assertTrue(containsRepresentative(environment, originalWrapper));
        });

        // Assert all container links are present
        for (LinkingResource originalLink : originalEnvironment.getLinkingResources__ResourceEnvironment()) {
            Set<Deployment> deployments = new HashSet<>();
            originalLink.getConnectedResourceContainers_LinkingResource()
                    .forEach(container -> deployments.add(new Deployment(container, false)));
            LinkResourceSpecification specification = new LinkResourceSpecification(
                    originalLink.getCommunicationLinkResourceSpecifications_LinkingResource(), false);
            assertTrue(containsRepresentative(environment, specification, deployments));
        }
    }

    protected Collection<Discoverer<?>> getDiscoverers() {
        PcmDiscovererCreator discovererCreator = new PcmDiscovererCreator(loadSystem(),
                loadAllocation(), loadResourceEnvironment());
        return discovererCreator.createDiscoverers();
    }

    protected abstract String getCaseStudyName();

    protected abstract File getRepositoryFile();

    protected abstract File getSystemFile();

    protected abstract File getAllocationFile();

    protected abstract File getResourceEnvironmentFile();

    protected Repository loadRepository() {
        String fileName = getRepositoryFile().getName();
        return ModelLoader.loadRepository(fileName);
    }

    protected System loadSystem() {
        String fileName = getSystemFile().getName();
        return ModelLoader.loadSystem(fileName);
    }

    protected Allocation loadAllocation() {
        String fileName = getAllocationFile().getName();
        return ModelLoader.loadAllocation(fileName);
    }

    protected ResourceEnvironment loadResourceEnvironment() {
        String fileName = getResourceEnvironmentFile().getName();
        return ModelLoader.loadResourceEnvironment(fileName);
    }
}
