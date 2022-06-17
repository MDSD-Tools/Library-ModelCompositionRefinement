package com.gstuer.modelmerging.instance.pcm.orchestration;

import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAllocationRelation;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PcmOrchestratorTest {
    @Test
    public void testExistsComponentAfterProcess() {
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Component component = new Component("TestComponent", false);
        orchestrator.processDiscovery(component);
        assertTrue(orchestrator.getModel().contains(component));
    }

    @Test
    public void testExistImplicitReplaceablesAfterProcess() {
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Component component = new Component("TestComponent", false);
        orchestrator.processDiscovery(component);

        PcmSurrogate model = orchestrator.getModel();
        List<Deployment> deployments = model.getByType(Deployment.class);
        Stream<ComponentAllocationRelation> componentDeploymentRelations = model.getByType(ComponentAllocationRelation.class).stream();

        // Assertions
        assertTrue(model.contains(component));
        assertTrue(componentDeploymentRelations.anyMatch(element -> element.getSource().equals(component)));
        assertFalse(deployments.isEmpty());
    }

    @Test
    public void testExistsDeploymentAfterProcess() {
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Deployment deployment = new Deployment("TestDeployment", false);
        orchestrator.processDiscovery(deployment);
        assertTrue(orchestrator.getModel().contains(deployment));
    }
}
