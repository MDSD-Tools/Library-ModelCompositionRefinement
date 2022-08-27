package com.gstuer.modelmerging.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import com.gstuer.modelmerging.framework.discovery.Discoverer;
import com.gstuer.modelmerging.instance.pcm.orchestration.PcmOrchestrator;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.transformation.AllocationTransformer;
import com.gstuer.modelmerging.instance.pcm.transformation.RepositoryTransformer;
import com.gstuer.modelmerging.instance.pcm.transformation.ResourceEnvironmentTransformer;

public class CoReXTest extends CaseStudyTest {
    private static final String FILE_PATH_PREFIX = "./resources/evaluation/case-studies/CoReX/";

    // region Refinement Evaluation - Generic

    @Override
    @ParameterizedTest
    @ValueSource(strings = { "_OdXCIByyEe2EUeCzYK37WA", "_OHlmcByyEe2EUeCzYK37WA" })
    public void testRefinementComponentWithoutContainer(String componentIdentifier) {
        super.testRefinementComponentWithoutContainer(componentIdentifier);
    }

    @Override
    @ParameterizedTest
    @ValueSource(strings = { "_Qn_GoByyEe2EUeCzYK37WA" })
    public void testRefinementInterfaceWithoutComponent(String interfaceIdentifier) {
        super.testRefinementInterfaceWithoutComponent(interfaceIdentifier);
    }

    @Override
    @ParameterizedTest
    @ValueSource(strings = { "_Di9fMBy0Ee2EUeCzYK37WA" })
    public void testRefinementComponentAssemblyRequiresContainerLink(String linkIdentifier) {
        super.testRefinementComponentAssemblyRequiresContainerLink(linkIdentifier);
    }

    @Override
    @ParameterizedTest
    @ValueSource(strings = { "_Di9fMBy0Ee2EUeCzYK37WA" })
    public void testRefinementContainerLinkNotReplacedIndirectly(String linkIdentifier) {
        super.testRefinementContainerLinkNotReplacedIndirectly(linkIdentifier);
    }

    // endregion

    // region Refinement Evaluation - CoReX only

    @Test
    public void testCoReXRefinementUnallocatedComponent() {
        // Initialize orchestrator and get discoverers for case study models
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Collection<Discoverer<?>> discoverers = getDiscoverers();
        AllocationTransformer transformer = new AllocationTransformer();

        // Process discoverers and transform output models
        discoverers.forEach(orchestrator::processDiscoverer);
        PcmSurrogate model = orchestrator.getModel();
        Allocation allocation = transformer.transform(model);

        // Assert component is allocated
        assertEquals(1L, allocation.getAllocationContexts_Allocation().stream()
                .filter(context -> context.getAssemblyContext_AllocationContext()
                        .getEncapsulatedComponent__AssemblyContext().getEntityName().equals("UnallocatedComponent")
                        && context.getResourceContainer_AllocationContext() != null)
                .count());
    }

    @Test
    public void testCoReXRefinementUnprovidedInterface() {
        // Initialize orchestrator and get discoverers for case study models
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Collection<Discoverer<?>> discoverers = getDiscoverers();
        RepositoryTransformer transformer = new RepositoryTransformer();

        // Process discoverers and transform output models
        discoverers.forEach(orchestrator::processDiscoverer);
        PcmSurrogate model = orchestrator.getModel();
        Repository repository = transformer.transform(model);

        // Assert interface has provider
        assertEquals(1L, repository.getComponents__Repository().stream()
                .flatMap(component -> component.getProvidedRoles_InterfaceProvidingEntity().stream())
                .filter(role -> role instanceof OperationProvidedRole)
                .map(role -> (OperationProvidedRole) role)
                .map(OperationProvidedRole::getProvidedInterface__OperationProvidedRole)
                .filter(interFace -> interFace.getEntityName().equals("OnlyRequiredInterface"))
                .count());
    }

    @Test
    public void testCoReXRefinementMissingSeffs() {
        // Initialize orchestrator and get discoverers for case study models
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Collection<Discoverer<?>> discoverers = getDiscoverers();
        RepositoryTransformer transformer = new RepositoryTransformer();

        // Process discoverers and transform output models
        discoverers.forEach(orchestrator::processDiscoverer);
        PcmSurrogate model = orchestrator.getModel();
        Repository repository = transformer.transform(model);

        // Assert missing seffs were added to seffless provider
        assertEquals(3L, repository.getComponents__Repository().stream()
                .filter(component -> component instanceof BasicComponent)
                .map(component -> (BasicComponent) component)
                .filter(component -> component.getEntityName().equals("SefflessProvider"))
                .flatMap(component -> component.getServiceEffectSpecifications__BasicComponent().stream())
                .count());
    }

    @Test
    public void testCoReXRefinementMissingContainerLink() {
        // Initialize orchestrator and get discoverers for case study models
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Collection<Discoverer<?>> discoverers = getDiscoverers();
        ResourceEnvironmentTransformer transformer = new ResourceEnvironmentTransformer();

        // Process discoverers and transform output models
        discoverers.forEach(orchestrator::processDiscoverer);
        PcmSurrogate model = orchestrator.getModel();
        ResourceEnvironment resourceEnvironment = transformer.transform(model);

        // Assert link between containers exist
        boolean linkExists = false;
        assertFalse(resourceEnvironment.getLinkingResources__ResourceEnvironment().isEmpty());
        for (LinkingResource linkingResource : resourceEnvironment.getLinkingResources__ResourceEnvironment()) {
            List<ResourceContainer> linkedContainers = new LinkedList<>(
                    linkingResource.getConnectedResourceContainers_LinkingResource());
            boolean containsContainers = true;
            for (String entityName : List.of("MissingLinkConsumer", "MissingLinkProvider")) {
                containsContainers = containsContainers
                        && linkedContainers.removeIf(element -> element.getEntityName().equals(entityName));
            }
            if (containsContainers) {
                linkExists = true;
                break;
            }
        }
        assertTrue(linkExists);
    }

    // endregion

    @Override
    protected String getCaseStudyName() {
        return "CoReX";
    }

    @Override
    protected File getRepositoryFile() {
        return new File(FILE_PATH_PREFIX + "corex.repository");
    }

    @Override
    protected File getSystemFile() {
        return new File(FILE_PATH_PREFIX + "corex.system");
    }

    @Override
    protected File getAllocationFile() {
        return new File(FILE_PATH_PREFIX + "corex.allocation");
    }

    @Override
    protected File getResourceEnvironmentFile() {
        return new File(FILE_PATH_PREFIX + "corex.resourceenvironment");
    }
}
