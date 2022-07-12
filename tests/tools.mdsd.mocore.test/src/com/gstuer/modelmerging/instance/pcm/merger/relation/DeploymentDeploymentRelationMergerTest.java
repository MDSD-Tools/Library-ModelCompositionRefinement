package com.gstuer.modelmerging.instance.pcm.merger.relation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.gstuer.modelmerging.framework.merger.RelationMergerTest;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAssemblyRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.DeploymentDeploymentRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.LinkResourceSpecificationRelation;
import com.gstuer.modelmerging.test.utility.ElementFactory;

public class DeploymentDeploymentRelationMergerTest extends RelationMergerTest<DeploymentDeploymentRelationMerger,
        PcmSurrogate, DeploymentDeploymentRelation, Deployment, Deployment> {
    @Test
    public void testRefineWithValidElementAddsCorrectImplications() {
        // Test data
        PcmSurrogate model = createEmptyModel();
        DeploymentDeploymentRelationMerger merger = createMerger(model);
        DeploymentDeploymentRelation relation = createUniqueReplaceable();

        // Assertions: Pre-execution
        assertTrue(merger.getImplications().isEmpty());

        // Execution
        merger.refine(relation);
        Set<Replaceable> implications = new HashSet<>(merger.getImplications());

        // Assertions: Post-execution
        assertEquals(2, implications.size());

        //// Implicit ComponentAssemblyRelation
        Replaceable implication = implications.stream()
                .filter(replacable -> replacable.getClass().equals(ComponentAssemblyRelation.class))
                .findFirst().orElseThrow();
        ComponentAssemblyRelation implicitAssembly = (ComponentAssemblyRelation) implication;
        assertTrue(implicitAssembly.isPlaceholder());
        assertTrue(implicitAssembly.getSource().isPlaceholder());
        assertTrue(implicitAssembly.getDestination().isPlaceholder());
        assertNotEquals(relation.getSource(), implicitAssembly.getSource().getSource());
        assertNotEquals(relation.getSource(), implicitAssembly.getSource().getDestination());
        assertNotEquals(relation.getDestination(), implicitAssembly.getSource().getSource());
        assertNotEquals(relation.getDestination(), implicitAssembly.getSource().getDestination());
        assertTrue(implications.remove(implication));

        //// Implicit LinkResourceSpecificationRelation
        assertEquals(1, implications.size());
        implication = implications.stream().findFirst().orElseThrow();
        assertEquals(LinkResourceSpecificationRelation.class, implication.getClass());
        LinkResourceSpecificationRelation implicitSpecification = (LinkResourceSpecificationRelation) implication;
        assertEquals(relation, implicitSpecification.getDestination());
        assertTrue(implicitSpecification.isPlaceholder());
        assertTrue(implicitSpecification.getSource().isPlaceholder());
    }

    @Override
    protected DeploymentDeploymentRelation createRelation(Deployment source, Deployment destination,
            boolean isPlaceholder) {
        return new DeploymentDeploymentRelation(source, destination, isPlaceholder);
    }

    @Override
    protected Deployment getUniqueNonPlaceholderSourceEntity() {
        return ElementFactory.createUniqueDeployment(false);
    }

    @Override
    protected Deployment getPlaceholderOfSourceEntity(Deployment source) {
        return new Deployment(source.getValue(), true);
    }

    @Override
    protected Deployment getUniqueNonPlaceholderDestinationEntity() {
        return getUniqueNonPlaceholderSourceEntity();
    }

    @Override
    protected Deployment getPlaceholderOfDestinationEntity(Deployment destination) {
        return getPlaceholderOfSourceEntity(destination);
    }

    @Override
    protected DeploymentDeploymentRelationMerger createMerger(PcmSurrogate model) {
        return new DeploymentDeploymentRelationMerger(model);
    }

    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }
}
