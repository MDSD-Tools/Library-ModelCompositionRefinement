package com.gstuer.modelmerging.evaluation;

import static com.gstuer.modelmerging.evaluation.discovery.PcmDiscovererCreator.createComponentFromRepositoryComponent;
import static com.gstuer.modelmerging.test.utility.PcmEvaluationUtility.containsRepresentative;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.palladiosimulator.generator.fluent.shared.util.ModelLoader;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

import com.gstuer.modelmerging.evaluation.discovery.PcmDiscovererCreator;
import com.gstuer.modelmerging.framework.discovery.Discoverer;
import com.gstuer.modelmerging.instance.pcm.orchestration.PcmOrchestrator;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.transformation.RepositoryTransformer;

public abstract class CaseStudyTest {
    @Test
    public void testTransformedRepositoryContainsRelevantElements() {
        // Initialize orchestrator and get discoverers for case study models
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Collection<Discoverer<?>> discoverers = getDiscoverers();
        RepositoryTransformer transformer = new RepositoryTransformer();
        Repository originalRepository = getRepository();

        // Process discoverers and transform output models
        discoverers.forEach(orchestrator::processDiscoverer);
        PcmSurrogate model = orchestrator.getModel();
        Repository repository = transformer.transform(model);

        // Assert all components are present
        originalRepository.getComponents__Repository().forEach(originalComponent -> {
            final Component originalWrapper = createComponentFromRepositoryComponent(originalComponent);
            assertTrue(containsRepresentative(repository, originalWrapper));
        });

        // Assert all interfaces & signatures are present
        // TODO

        // Assert all interface provisions are present
        // TODO

        // Assert all interface requirements are present
        // TODO

        // Assert all seffs are present
        // TODO
    }

    protected Collection<Discoverer<?>> getDiscoverers() {
        PcmDiscovererCreator discovererCreator = new PcmDiscovererCreator(getRepository(), getSystem(), getAllocation(),
                getResourceEnvironment());
        return discovererCreator.createDiscoverers();
    }

    protected abstract Repository getRepository();

    protected abstract System getSystem();

    protected abstract Allocation getAllocation();

    protected abstract ResourceEnvironment getResourceEnvironment();

    protected static Repository loadRepositoryFromFile(File file) {
        return ModelLoader.loadRepository(file.getAbsolutePath());
    }

    protected static System loadSystemFromFile(File file) {
        return ModelLoader.loadSystem(file.getAbsolutePath());
    }

    protected static Allocation loadAllocationFromFile(File file) {
        return ModelLoader.loadAllocation(file.getAbsolutePath());
    }

    protected static ResourceEnvironment loadResourceEnvironmentFromFile(File file) {
        return ModelLoader.loadResourceEnvironment(file.getAbsolutePath());
    }
}
