package com.gstuer.modelmerging.instance.pcm.merger.relation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;

import com.gstuer.modelmerging.framework.merger.RelationMergerTest;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Component;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Interface;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAllocationRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.ComponentAssemblyRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.DeploymentDeploymentRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceProvisionRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.InterfaceRequirementRelation;

public class ComponentAssemblyRelationMergerTest extends RelationMergerTest<ComponentAssemblyRelationMerger,
        PcmSurrogate, ComponentAssemblyRelation, InterfaceProvisionRelation, InterfaceRequirementRelation> {
    private static final Interface RELATION_DESTINATION = Interface.getUniquePlaceholder();

    @Test
    @DisabledIf(TEST_API_ONLY_METHOD_NAME)
    public void testRefinementRemovesParallelAssemblyPlaceholder() {
        // Test data
        PcmSurrogate model = createEmptyModel();
        ComponentAssemblyRelationMerger merger = createMerger(model);

        InterfaceProvisionRelation interfaceProvision = getUniqueNonPlaceholderSourceEntity();
        InterfaceRequirementRelation interfaceRequirement = getUniqueNonPlaceholderDestinationEntity();
        ComponentAssemblyRelation relation = createRelation(interfaceProvision, interfaceRequirement, false);

        Deployment providingContainer = Deployment.getUniquePlaceholder();
        Deployment requiringContainer = Deployment.getUniquePlaceholder();
        ComponentAllocationRelation providingAllocation = new ComponentAllocationRelation(
                interfaceProvision.getSource(), providingContainer, false);
        ComponentAllocationRelation requiringAllocation = new ComponentAllocationRelation(
                interfaceRequirement.getSource(), requiringContainer, false);

        InterfaceProvisionRelation placeholderProvision = getPlaceholderOfSourceEntity(
                getUniqueNonPlaceholderSourceEntity());
        InterfaceRequirementRelation placeholderRequirement = getPlaceholderOfDestinationEntity(
                getUniqueNonPlaceholderDestinationEntity());
        ComponentAllocationRelation placeholderProvidingAllocation = new ComponentAllocationRelation(
                placeholderProvision.getSource(), providingContainer, false);
        ComponentAllocationRelation placeholderRequiringAllocation = new ComponentAllocationRelation(
                placeholderRequirement.getSource(), requiringContainer, false);
        ComponentAssemblyRelation placeholderRelation = createRelation(placeholderProvision,
                placeholderRequirement, true);

        // Add containers, placeholder assembly & allocations to model
        model.add(providingContainer);
        model.add(requiringContainer);
        model.add(providingAllocation);
        model.add(requiringAllocation);

        model.add(placeholderProvision.getSource());
        model.add(placeholderProvision.getDestination());
        model.add(placeholderRequirement.getSource());
        model.add(placeholderProvision);
        model.add(placeholderRequirement);
        model.add(placeholderProvidingAllocation);
        model.add(placeholderRequiringAllocation);
        model.add(placeholderRelation);

        // Assertions: Pre-execution
        assertTrue(merger.getImplications().isEmpty());
        assertTrue(model.contains(placeholderProvision.getSource()));
        assertTrue(model.contains(placeholderProvision.getDestination()));
        assertTrue(model.contains(placeholderRequirement.getSource()));
        assertTrue(model.contains(placeholderProvision));
        assertTrue(model.contains(placeholderRequirement));
        assertTrue(model.contains(placeholderProvidingAllocation));
        assertTrue(model.contains(placeholderRequiringAllocation));
        assertTrue(model.contains(placeholderRelation));

        // Execution
        merger.refine(relation);
        Set<Replaceable> implications = new HashSet<>(merger.getImplications());

        // Assertions: Post-execution
        assertFalse(model.contains(placeholderProvision.getSource()));
        assertFalse(model.contains(placeholderProvision.getDestination()));
        assertFalse(model.contains(placeholderRequirement.getSource()));
        assertFalse(model.contains(placeholderProvision));
        assertFalse(model.contains(placeholderRequirement));
        assertFalse(model.contains(placeholderProvidingAllocation));
        assertFalse(model.contains(placeholderRequiringAllocation));
        assertFalse(model.contains(placeholderRelation));

        assertTrue(implications.contains(relation));
        assertTrue(implications.contains(interfaceProvision.getSource()));
        assertTrue(implications.contains(interfaceProvision.getDestination()));
        assertTrue(implications.contains(interfaceRequirement.getSource()));
    }

    @Test
    @DisabledIf(TEST_API_ONLY_METHOD_NAME)
    public void testRefinementAddsImplicitDeploymentRelation() {
        // Test data
        PcmSurrogate model = createEmptyModel();
        ComponentAssemblyRelationMerger merger = createMerger(model);

        InterfaceProvisionRelation interfaceProvision = getUniqueNonPlaceholderSourceEntity();
        InterfaceRequirementRelation interfaceRequirement = getUniqueNonPlaceholderDestinationEntity();
        ComponentAssemblyRelation relation = createRelation(interfaceProvision, interfaceRequirement, false);

        Deployment providingContainer = Deployment.getUniquePlaceholder();
        Deployment requiringContainer = Deployment.getUniquePlaceholder();
        ComponentAllocationRelation providingAllocation = new ComponentAllocationRelation(
                interfaceProvision.getSource(), providingContainer, false);
        ComponentAllocationRelation requiringAllocation = new ComponentAllocationRelation(
                interfaceRequirement.getSource(), requiringContainer, false);

        // Add containers & allocations to model
        model.add(providingContainer);
        model.add(requiringContainer);
        model.add(providingAllocation);
        model.add(requiringAllocation);

        // Assertions: Pre-execution
        assertTrue(merger.getImplications().isEmpty());

        // Execution
        merger.refine(relation);
        Set<Replaceable> implications = new HashSet<>(merger.getImplications());

        // Assertions: Post-execution
        assertTrue(implications.remove(relation.getSource()));
        assertTrue(implications.remove(relation.getDestination()));
        assertEquals(1, implications.size());
        Replaceable implication = implications.stream().findFirst().orElseThrow();
        assertEquals(DeploymentDeploymentRelation.class, implication.getClass());
        DeploymentDeploymentRelation implicitDeploymentLink = (DeploymentDeploymentRelation) implication;
        assertEquals(providingContainer, implicitDeploymentLink.getSource());
        assertEquals(requiringContainer, implicitDeploymentLink.getDestination());
        assertTrue(implicitDeploymentLink.isPlaceholder());
    }

    @Override
    protected ComponentAssemblyRelation createRelation(InterfaceProvisionRelation source,
            InterfaceRequirementRelation destination,
            boolean isPlaceholder) {
        return new ComponentAssemblyRelation(source, destination, isPlaceholder);
    }

    @Override
    protected InterfaceProvisionRelation getUniqueNonPlaceholderSourceEntity() {
        Component source = Component.getUniquePlaceholder();
        return new InterfaceProvisionRelation(source, RELATION_DESTINATION, false);
    }

    @Override
    protected InterfaceProvisionRelation getPlaceholderOfSourceEntity(InterfaceProvisionRelation source) {
        return new InterfaceProvisionRelation(source.getSource(), source.getDestination(), true);
    }

    @Override
    protected InterfaceRequirementRelation getUniqueNonPlaceholderDestinationEntity() {
        Component source = Component.getUniquePlaceholder();
        return new InterfaceRequirementRelation(source, RELATION_DESTINATION, false);
    }

    @Override
    protected InterfaceRequirementRelation getPlaceholderOfDestinationEntity(InterfaceRequirementRelation destination) {
        return new InterfaceRequirementRelation(destination.getSource(), destination.getDestination(), true);
    }

    @Override
    protected ComponentAssemblyRelationMerger createMerger(PcmSurrogate model) {
        return new ComponentAssemblyRelationMerger(model);
    }

    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }
}
