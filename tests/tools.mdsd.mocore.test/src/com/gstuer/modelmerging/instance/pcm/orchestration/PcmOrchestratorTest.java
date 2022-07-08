package com.gstuer.modelmerging.instance.pcm.orchestration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAllocationRelation;
import com.gstuer.modelmerging.test.utility.ElementFactory;

public class PcmOrchestratorTest {
    @Test
    public void testExistsComponentAfterProcess() {
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Component component = ElementFactory.createUniqueComponent(false);
        orchestrator.processDiscovery(component);
        assertTrue(orchestrator.getModel().contains(component));
    }

    @Test
    public void testExistImplicitReplaceablesAfterProcess() {
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Component component = ElementFactory.createUniqueComponent(false);
        orchestrator.processDiscovery(component);

        PcmSurrogate model = orchestrator.getModel();
        List<Deployment> deployments = model.getByType(Deployment.class);
        Stream<ComponentAllocationRelation> componentDeploymentRelations = model
                .getByType(ComponentAllocationRelation.class).stream();

        // Assertions
        assertTrue(model.contains(component));
        assertTrue(componentDeploymentRelations.anyMatch(element -> element.getSource().equals(component)));
        assertFalse(deployments.isEmpty());
    }

    @Test
    public void testExistsDeploymentAfterProcess() {
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Deployment deployment = ElementFactory.createUniqueDeployment(false);
        orchestrator.processDiscovery(deployment);
        assertTrue(orchestrator.getModel().contains(deployment));
    }
}
