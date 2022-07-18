package com.gstuer.modelmerging.instance.pcm.transformation;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import com.gstuer.modelmerging.framework.transformation.TransformerTest;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.Deployment;
import com.gstuer.modelmerging.instance.pcm.surrogate.element.LinkResourceSpecification;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.DeploymentDeploymentRelation;
import com.gstuer.modelmerging.instance.pcm.surrogate.relation.LinkResourceSpecificationRelation;

public class ResourceEnvironmentTransformerTest
        extends TransformerTest<ResourceEnvironmentTransformer, PcmSurrogate, ResourceEnvironment> {
    @Test
    public void testTransformIndependentPlaceholderContainers() {
        // Test data
        ResourceEnvironmentTransformer transformer = createTransformer();
        PcmSurrogate model = createEmptyModel();
        Deployment fstDeployment = Deployment.getUniquePlaceholder();
        Deployment sndDeployment = Deployment.getUniquePlaceholder();
        Deployment trdDeployment = Deployment.getUniquePlaceholder();
        Deployment fthDeployment = Deployment.getUniquePlaceholder();

        model.add(fstDeployment);
        model.add(sndDeployment);
        model.add(trdDeployment);
        model.add(fthDeployment);

        // Execution
        ResourceEnvironment environment = transformer.transform(model);

        // Assertion
        assertNotNull(environment);
        // TODO Check whether all elements where transformed correctly
    }

    @Test
    public void testTransformConnectedPlaceholderContainers() {
        // Test data
        ResourceEnvironmentTransformer transformer = createTransformer();
        PcmSurrogate model = createEmptyModel();
        Deployment fstDeployment = Deployment.getUniquePlaceholder();
        Deployment sndDeployment = Deployment.getUniquePlaceholder();
        Deployment trdDeployment = Deployment.getUniquePlaceholder();

        DeploymentDeploymentRelation fstLinkRelation = new DeploymentDeploymentRelation(fstDeployment,
                sndDeployment, false);
        DeploymentDeploymentRelation sndLinkRelation = new DeploymentDeploymentRelation(sndDeployment,
                trdDeployment, true);

        LinkResourceSpecification fstLinkSpecification = LinkResourceSpecification.getUniquePlaceholder();
        LinkResourceSpecificationRelation fstLinkSpecificationRelation = new LinkResourceSpecificationRelation(
                fstLinkSpecification, fstLinkRelation, false);

        LinkResourceSpecification sndLinkSpecification = LinkResourceSpecification.getUniquePlaceholder();
        LinkResourceSpecificationRelation sndLinkSpecificationRelation = new LinkResourceSpecificationRelation(
                sndLinkSpecification, sndLinkRelation, true);

        model.add(fstDeployment);
        model.add(sndDeployment);
        model.add(trdDeployment);
        model.add(fstLinkRelation);
        model.add(sndLinkRelation);
        model.add(fstLinkSpecificationRelation);
        model.add(sndLinkSpecificationRelation);

        // Execution
        ResourceEnvironment environment = transformer.transform(model);

        // Assertion
        assertNotNull(environment);
        // TODO Check whether all elements where transformed correctly
    }

    @Override
    protected ResourceEnvironmentTransformer createTransformer() {
        return new ResourceEnvironmentTransformer();
    }

    @Override
    protected PcmSurrogate createEmptyModel() {
        return new PcmSurrogate();
    }
}
