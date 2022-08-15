package com.gstuer.modelmerging.evaluation;

import static com.gstuer.modelmerging.evaluation.discovery.PcmDiscovererCreator.createComponentFromRepositoryComponent;
import static com.gstuer.modelmerging.test.utility.PcmEvaluationUtility.containsRepresentative;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.palladiosimulator.generator.fluent.shared.util.ModelLoader;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

import com.google.common.io.Files;
import com.gstuer.modelmerging.evaluation.discovery.PcmDiscovererCreator;
import com.gstuer.modelmerging.framework.discovery.Discoverer;
import com.gstuer.modelmerging.instance.pcm.orchestration.PcmOrchestrator;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.transformation.RepositoryTransformer;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class CaseStudyTest {
    @BeforeAll
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
        Repository originalRepository = loadRepository();

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
        PcmDiscovererCreator discovererCreator = new PcmDiscovererCreator(loadRepository(), loadSystem(),
                loadAllocation(), loadResourceEnvironment());
        return discovererCreator.createDiscoverers();
    }

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
