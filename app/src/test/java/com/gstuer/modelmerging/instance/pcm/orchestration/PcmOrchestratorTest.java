package com.gstuer.modelmerging.instance.pcm.orchestration;

import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PcmOrchestratorTest {
    @Test
    public void testExistsSingleComponentAfterProcess() {
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Component component = new Component("TestComponent", false);
        orchestrator.processDiscovery(component);
        assertTrue(orchestrator.getModel().contains(component));
    }

    @Test
    public void testExistsSingleDeploymentAfterProcess() {
        PcmOrchestrator orchestrator = new PcmOrchestrator();
        Deployment deployment = new Deployment("TestDeployment", false);
        orchestrator.processDiscovery(deployment);
        assertTrue(orchestrator.getModel().contains(deployment));
    }
}
