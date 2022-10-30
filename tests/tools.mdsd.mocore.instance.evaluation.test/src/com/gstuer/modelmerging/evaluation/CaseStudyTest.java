package com.gstuer.modelmerging.evaluation;

import static com.gstuer.modelmerging.evaluation.discovery.PcmDiscovererCreator.createComponentFromRepositoryComponent;
import static com.gstuer.modelmerging.instance.pcm.utility.PcmEvaluationUtility.containsRepresentative;
import static com.gstuer.modelmerging.instance.pcm.utility.PcmEvaluationUtility.representSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
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
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;
import org.palladiosimulator.pcm.system.System;

import com.google.common.io.Files;
import com.gstuer.modelmerging.evaluation.discovery.PcmDiscoverer;
import com.gstuer.modelmerging.evaluation.discovery.PcmDiscovererCreator;
import com.gstuer.modelmerging.framework.discovery.Discoverer;
import com.gstuer.modelmerging.framework.surrogate.Relation;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;
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
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.DeploymentDeploymentRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceProvisionRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceRequirementRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.LinkResourceSpecificationRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ServiceEffectSpecificationRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.SignatureProvisionRelation;
import com.gstuer.modelmerging.instance.pcm.transformation.AllocationTransformer;
import com.gstuer.modelmerging.instance.pcm.transformation.RepositoryTransformer;
import com.gstuer.modelmerging.instance.pcm.transformation.ResourceEnvironmentTransformer;
import com.gstuer.modelmerging.instance.pcm.transformation.SystemTransformer;
import com.gstuer.modelmerging.instance.pcm.utility.ElementFactory;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class CaseStudyTest {
    private static final boolean PRINT_INFOS = false;

    @BeforeAll
    public void setupTest() throws IOException {
        copyFiles();
        resetExtensionProcessor();
        printInfos();
    }

    public void printInfos() {
        if (!PRINT_INFOS) {
            return;
        }
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
        Set<?> linkResourceSpecifications = discoveries.stream()
                .filter(discovery -> discovery instanceof LinkResourceSpecificationRelation)
                .map(relation -> ((LinkResourceSpecificationRelation) relation).getSource())
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
        builder.append("SEFF: " + seffs.size() + "->"
                + repository.getComponents__Repository().stream()
                        .filter(component -> component instanceof BasicComponent)
                        .flatMap(component -> ((BasicComponent) component)
                                .getServiceEffectSpecifications__BasicComponent().stream())
                        .count());
        builder.append(java.lang.System.lineSeparator());
        builder.append("CLRS: " + linkResourceSpecifications.size() + "->"
                + resourceEnvironment.getLinkingResources__ResourceEnvironment().stream()
                        .map(link -> link.getCommunicationLinkResourceSpecifications_LinkingResource())
                        .distinct()
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

    // region Composition Evaluation

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
                        .collect(Collectors.toList());
                List<SignatureProvisionRelation> signatureProvisions = signatures.stream()
                        .map(signature -> new SignatureProvisionRelation(signature, interFace, false))
                        .collect(Collectors.toList());
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
                            .collect(Collectors.toList());
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

    // endregion

    // region Refinement Evaluation

    @Test
    public void testRefinementComponentWithoutContainer(String componentIdentifier) {
        // Initialize orchestrator and get discoverers for case study models
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Collection<Discoverer<?>> discoverers = new LinkedList<>(getDiscoverers());
        AllocationTransformer transformer = new AllocationTransformer();

        // Get original container of component
        Repository repository = loadRepository();
        RepositoryComponent repositoryComponent = repository.getComponents__Repository().stream()
                .filter(component -> component.getId().equals(componentIdentifier))
                .findFirst()
                .orElseThrow();
        Component component = createComponentFromRepositoryComponent(repositoryComponent);

        // Remove component allocation from discoveries
        Set<Replaceable> discoveries = new HashSet<>(discoverers.stream()
                .flatMap(discoverer -> discoverer.getDiscoveries().stream())
                .collect(Collectors.toSet()));
        Collection<ComponentAllocationRelation> allocations = discoveries.stream()
                .filter(discovery -> discovery instanceof ComponentAllocationRelation)
                .map(discovery -> (ComponentAllocationRelation) discovery)
                .filter(relation -> relation.getSource().equals(component))
                .collect(Collectors.toList());
        assertFalse(allocations.isEmpty());
        allocations.forEach(allocation -> assertTrue(discoveries.remove(allocation)));

        // Process discoveries and transform output model
        discoveries.forEach(orchestrator::processDiscovery);
        PcmSurrogate model = orchestrator.getModel();
        Allocation allocation = transformer.transform(model);

        // Assert a placeholder allocation with placeholder container was created
        assertEquals(1L, allocation.getAllocationContexts_Allocation().stream()
                .filter(context -> representSame(component.getValue(),
                        context.getAssemblyContext_AllocationContext().getEncapsulatedComponent__AssemblyContext())
                        && context.getResourceContainer_AllocationContext().getEntityName().startsWith("Placeholder_"))
                .count());
    }

    @Test
    public void testRefinementInterfaceWithoutComponent(String interfaceIdentifier) {
        // Initialize orchestrator and get discoverers for case study models
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Collection<Discoverer<?>> discoverers = new LinkedList<>(getDiscoverers());
        RepositoryTransformer transformer = new RepositoryTransformer();

        // Get interface wrapper and remove provisions from discoveries
        Set<Replaceable> discoveries = new HashSet<>(discoverers.stream()
                .flatMap(discoverer -> discoverer.getDiscoveries().stream())
                .collect(Collectors.toSet()));
        List<InterfaceProvisionRelation> interfaceProvisions = discoveries.stream()
                .filter(discovery -> discovery instanceof InterfaceProvisionRelation)
                .map(discovery -> (InterfaceProvisionRelation) discovery)
                .filter(provision -> provision.getDestination().getIdentifier().equals(interfaceIdentifier))
                .collect(Collectors.toList());
        assertFalse(interfaceProvisions.isEmpty());
        Interface interfaceWrapper = interfaceProvisions.get(0).getDestination();
        interfaceProvisions.forEach(provision -> assertTrue(discoveries.remove(provision)));

        // Remove seff relations and component assembly relations to avoid information conflict
        discoveries.removeIf(discovery -> discovery instanceof ServiceEffectSpecificationRelation);
        discoveries.removeIf(discovery -> discovery instanceof ComponentAssemblyRelation);

        // Process discoveries and transform output model
        discoveries.forEach(orchestrator::processDiscovery);
        PcmSurrogate model = orchestrator.getModel();
        Repository repository = transformer.transform(model);

        // Assert a placeholder allocation with placeholder container was created
        assertEquals(1L, repository.getComponents__Repository().stream()
                .filter(component -> component instanceof BasicComponent)
                .map(component -> (BasicComponent) component)
                .flatMap(component -> component.getProvidedRoles_InterfaceProvidingEntity().stream())
                .filter(role -> role instanceof OperationProvidedRole)
                .map(role -> (OperationProvidedRole) role)
                .map(OperationProvidedRole::getProvidedInterface__OperationProvidedRole)
                .filter(interFace -> representSame(interfaceWrapper.getValue(), interFace))
                .count());
    }

    @Test
    public void testRefinementSignatureWithoutInterface() {
        // Initialize orchestrator and get discoverers for case study models
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Collection<Discoverer<?>> discoverers = new LinkedList<>(getDiscoverers());
        RepositoryTransformer transformer = new RepositoryTransformer();

        // Get discoveries and add orphan signature
        Set<Replaceable> discoveries = new HashSet<>(discoverers.stream()
                .flatMap(discoverer -> discoverer.getDiscoveries().stream())
                .collect(Collectors.toSet()));
        Signature signature = ElementFactory.createUniqueSignature(false);
        assertTrue(discoveries.add(signature));

        // Process discoveries and transform output model
        discoveries.forEach(orchestrator::processDiscovery);
        PcmSurrogate model = orchestrator.getModel();
        Repository repository = transformer.transform(model);

        // Assert interface for signature was created
        assertEquals(1L, repository.getInterfaces__Repository().stream()
                .filter(interFace -> interFace instanceof OperationInterface)
                .map(interFace -> (OperationInterface) interFace)
                .flatMap(interFace -> interFace.getSignatures__OperationInterface().stream())
                .filter(interfaceSignature -> representSame(signature.getValue(), interfaceSignature))
                .count());
    }

    @Test
    public void testRefinementSignatureWithoutServiceEffectSpecification() {
        // Initialize orchestrator and get discoverers for case study models
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Collection<Discoverer<?>> discoverers = new LinkedList<>(getDiscoverers());
        RepositoryTransformer transformer = new RepositoryTransformer();

        // Get discoveries from discoverers
        Set<Replaceable> discoveries = new HashSet<>(discoverers.stream()
                .flatMap(discoverer -> discoverer.getDiscoveries().stream())
                .collect(Collectors.toSet()));

        // Assume case study has signatures
        assumeTrue(discoveries.stream().anyMatch(discovery -> discovery instanceof SignatureProvisionRelation));

        // Remove seff relations from discoveries
        assertTrue(discoveries.removeIf(discovery -> discovery instanceof ServiceEffectSpecificationRelation));
        discoveries.forEach(discovery -> assertFalse(discovery instanceof ServiceEffectSpecificationRelation));

        // Process discoveries and transform output model
        discoveries.forEach(orchestrator::processDiscovery);
        PcmSurrogate model = orchestrator.getModel();
        Repository repository = transformer.transform(model);

        // Assert seffs were created
        for (RepositoryComponent repositoryComponent : repository.getComponents__Repository()) {
            if (repositoryComponent instanceof BasicComponent) {
                BasicComponent component = (BasicComponent) repositoryComponent;
                List<org.palladiosimulator.pcm.seff.ServiceEffectSpecification> seffs = component
                        .getServiceEffectSpecifications__BasicComponent();
                for (ProvidedRole role : component.getProvidedRoles_InterfaceProvidingEntity()) {
                    if (role instanceof OperationProvidedRole) {
                        OperationProvidedRole operationRole = (OperationProvidedRole) role;
                        OperationInterface operationInterface = operationRole
                                .getProvidedInterface__OperationProvidedRole();
                        for (OperationSignature signature : operationInterface.getSignatures__OperationInterface()) {
                            assertTrue(seffs.stream()
                                    .anyMatch(seff -> representSame(signature, seff.getDescribedService__SEFF())));
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testRefinementContainerLinkWithoutLinkSpecification() {
        // Initialize orchestrator and get discoverers for case study models
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Collection<Discoverer<?>> discoverers = new LinkedList<>(getDiscoverers());
        ResourceEnvironmentTransformer transformer = new ResourceEnvironmentTransformer();

        // Get discoveries from discoverers
        Set<Replaceable> discoveries = new HashSet<>(discoverers.stream()
                .flatMap(discoverer -> discoverer.getDiscoveries().stream())
                .collect(Collectors.toSet()));

        // Assume case study has connected containers
        assumeTrue(discoveries.stream().anyMatch(discovery -> discovery instanceof LinkResourceSpecificationRelation));

        // Remove link specification relations and add unspecified links
        List<DeploymentDeploymentRelation> unspecifiedLinks = discoveries.stream()
                .filter(discovery -> discovery instanceof LinkResourceSpecificationRelation)
                .map(discovery -> (LinkResourceSpecificationRelation) discovery)
                .map(relation -> relation.getDestination())
                .collect(Collectors.toList());
        assertTrue(discoveries.removeIf(discovery -> discovery instanceof LinkResourceSpecificationRelation));
        discoveries.forEach(discovery -> assertFalse(discovery instanceof LinkResourceSpecificationRelation));
        discoveries.forEach(discovery -> assertFalse(discovery instanceof LinkResourceSpecification));
        assertTrue(discoveries.addAll(unspecifiedLinks));

        // Process discoveries and transform output model
        discoveries.forEach(orchestrator::processDiscovery);
        PcmSurrogate model = orchestrator.getModel();
        ResourceEnvironment resourceEnvironment = transformer.transform(model);

        // Assert specifications of links exist
        for (LinkingResource link : resourceEnvironment.getLinkingResources__ResourceEnvironment()) {
            assertNotNull(link.getCommunicationLinkResourceSpecifications_LinkingResource());
        }
    }

    @Test
    public void testRefinementOrphanedLinkSpecification() {
        // Initialize orchestrator and get discoverers for case study models
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Collection<Discoverer<?>> discoverers = new LinkedList<>(getDiscoverers());
        ResourceEnvironmentTransformer transformer = new ResourceEnvironmentTransformer();

        // Get discoveries from discoverers and add specification orphan
        Set<Replaceable> discoveries = new HashSet<>(discoverers.stream()
                .flatMap(discoverer -> discoverer.getDiscoveries().stream())
                .collect(Collectors.toSet()));
        LinkResourceSpecification orphanSpecification = ElementFactory.createUniqueLinkResourceSpecification(false);
        discoveries.add(orphanSpecification);

        // Process discoveries and transform output model
        discoveries.forEach(orchestrator::processDiscovery);
        PcmSurrogate model = orchestrator.getModel();
        ResourceEnvironment resourceEnvironment = transformer.transform(model);

        // Assert two placeholder containers and a link between them was created
        boolean existsLink = false;
        boolean areContainersPlaceholders = false;
        for (LinkingResource link : resourceEnvironment.getLinkingResources__ResourceEnvironment()) {
            if (link.getCommunicationLinkResourceSpecifications_LinkingResource().getId()
                    .equals(orphanSpecification.getIdentifier())) {
                existsLink = true;
                areContainersPlaceholders = link.getConnectedResourceContainers_LinkingResource().stream()
                        .allMatch(container -> container.getEntityName().startsWith("Placeholder_"));
                break;
            }
        }
        assertTrue(existsLink);
        assertTrue(areContainersPlaceholders);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRefinementComponentAssemblyRequiresContainerLink(String linkIdentifier) {
        // Initialize orchestrator and get discoverers for case study models
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Collection<Discoverer<?>> discoverers = new LinkedList<>(getDiscoverers());
        ResourceEnvironmentTransformer transformer = new ResourceEnvironmentTransformer();

        // Get original link from resource environment and create wrapper
        ResourceEnvironment originalEnvironment = loadResourceEnvironment();
        List<LinkingResource> originalLinks = new LinkedList<>(
                originalEnvironment.getLinkingResources__ResourceEnvironment());
        originalLinks.removeIf(link -> !link.getId().equals(linkIdentifier));
        assertEquals(1, originalLinks.size());
        LinkingResource originalLink = originalLinks.get(0);
        ResourceContainer source = originalLink.getConnectedResourceContainers_LinkingResource().get(0);
        ResourceContainer destination = originalLink.getConnectedResourceContainers_LinkingResource().get(1);
        DeploymentDeploymentRelation deploymentRelation = new DeploymentDeploymentRelation(
                new Deployment(source, false), new Deployment(destination, false), false);
        LinkResourceSpecification linkSpecification = new LinkResourceSpecification(
                originalLink.getCommunicationLinkResourceSpecifications_LinkingResource(), false);
        LinkResourceSpecificationRelation linkRelation = new LinkResourceSpecificationRelation(
                linkSpecification, deploymentRelation, false);

        // Remove original link from discoverers
        Discoverer<?> oldDiscoverer = discoverers.stream()
                .filter(disco -> disco.getDiscoveryType().equals(LinkResourceSpecificationRelation.class))
                .findFirst()
                .orElseThrow();
        Set<LinkResourceSpecificationRelation> discoveries = new HashSet<>((Set<
                LinkResourceSpecificationRelation>) oldDiscoverer.getDiscoveries());
        assertTrue(discoveries.remove(linkRelation));
        assertTrue(discoverers.remove(oldDiscoverer));
        PcmDiscoverer<LinkResourceSpecificationRelation> linkRelationDiscoverer = new PcmDiscoverer<>(discoveries,
                LinkResourceSpecificationRelation.class);
        assertTrue(discoverers.add(linkRelationDiscoverer));

        // Process discoverers and transform output models
        discoverers.forEach(orchestrator::processDiscoverer);
        PcmSurrogate model = orchestrator.getModel();
        ResourceEnvironment resourceEnvironment = transformer.transform(model);

        // Assert placeholder link exists
        DeploymentDeploymentRelation sourceDestinationLinkPlaceholder = new DeploymentDeploymentRelation(
                new Deployment(source, false), new Deployment(destination, false), true);
        assertTrue(containsRepresentative(resourceEnvironment, sourceDestinationLinkPlaceholder));
    }

    @Test
    public void testRefinementContainerLinkNotReplacedIndirectly(String linkIdentifier) {
        // Initialize orchestrator and get discoverers for case study models
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Collection<Discoverer<?>> discoverers = getDiscoverers();
        ResourceEnvironmentTransformer transformer = new ResourceEnvironmentTransformer();

        // Get original link from resource environment
        ResourceEnvironment originalEnvironment = loadResourceEnvironment();
        List<LinkingResource> originalLinks = new LinkedList<>(
                originalEnvironment.getLinkingResources__ResourceEnvironment());
        originalLinks.removeIf(link -> !link.getId().equals(linkIdentifier));
        assertEquals(1, originalLinks.size());
        LinkingResource originalLink = originalLinks.get(0);

        // Get the first two containers of the original link and create indirect placeholders
        assertTrue(originalLink.getConnectedResourceContainers_LinkingResource().size() >= 2);
        ResourceContainer source = originalLink.getConnectedResourceContainers_LinkingResource().get(0);
        ResourceContainer destination = originalLink.getConnectedResourceContainers_LinkingResource().get(1);
        DeploymentDeploymentRelation sourceDestinationLink = new DeploymentDeploymentRelation(
                new Deployment(source, false), new Deployment(destination, false), false);
        DeploymentDeploymentRelation indirectSourcePlaceholderLink = new DeploymentDeploymentRelation(
                new Deployment(source, false), Deployment.getUniquePlaceholder(), true);
        DeploymentDeploymentRelation indirectDestinationPlaceholderLink = new DeploymentDeploymentRelation(
                Deployment.getUniquePlaceholder(), new Deployment(destination, false), true);

        // Process indirect placeholder links
        orchestrator.processDiscovery(indirectSourcePlaceholderLink);
        orchestrator.processDiscovery(indirectDestinationPlaceholderLink);

        // Process discoverers and transform output models
        discoverers.forEach(orchestrator::processDiscoverer);
        PcmSurrogate model = orchestrator.getModel();
        ResourceEnvironment resourceEnvironment = transformer.transform(model);

        // Assert real link and indirect placeholders are present in model
        assertTrue(containsRepresentative(resourceEnvironment, sourceDestinationLink.getSource()));
        assertTrue(containsRepresentative(resourceEnvironment, sourceDestinationLink.getDestination()));
        assertTrue(containsRepresentative(resourceEnvironment, indirectSourcePlaceholderLink.getDestination()));
        assertTrue(containsRepresentative(resourceEnvironment, indirectDestinationPlaceholderLink.getSource()));
        assertTrue(containsRepresentative(resourceEnvironment, sourceDestinationLink));
        assertTrue(containsRepresentative(resourceEnvironment, indirectSourcePlaceholderLink));
        assertTrue(containsRepresentative(resourceEnvironment, indirectDestinationPlaceholderLink));
    }

    // endregion

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
