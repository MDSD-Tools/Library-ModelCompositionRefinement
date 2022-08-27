package com.gstuer.modelmerging.evaluation;

import java.io.File;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class MinimumProjectTest extends CaseStudyTest {
    private static final String FILE_PATH_PREFIX = "./resources/evaluation/case-studies/palladio-example-models/"
            + "Minimum_Project_Example/";

    @Override
    @ParameterizedTest
    @ValueSource(strings = { "_os1t4CHbEd62GabW1zGSBw" })
    public void testRefinementComponentWithoutContainer(String componentIdentifier) {
        super.testRefinementComponentWithoutContainer(componentIdentifier);
    }

    @Override
    @ParameterizedTest
    @ValueSource(strings = { "_n7g-oCHbEd62GabW1zGSBw" })
    public void testRefinementInterfaceWithoutComponent(String interfaceIdentifier) {
        super.testRefinementInterfaceWithoutComponent(interfaceIdentifier);
    }

    @Override
    @Disabled("The case study does not provide a container link.")
    public void testRefinementComponentAssemblyRequiresContainerLink(String linkIdentifier) {
        super.testRefinementComponentAssemblyRequiresContainerLink(linkIdentifier);
    }

    @Override
    @Disabled("The case study does not provide a container link.")
    public void testRefinementContainerLinkNotReplacedIndirectly(String linkIdentifier) {
        super.testRefinementContainerLinkNotReplacedIndirectly(linkIdentifier);
    }

    @Override
    protected String getCaseStudyName() {
        return "Minimum Project";
    }

    @Override
    protected File getRepositoryFile() {
        return new File(FILE_PATH_PREFIX + "default.repository");
    }

    @Override
    protected File getSystemFile() {
        return new File(FILE_PATH_PREFIX + "default.system");
    }

    @Override
    protected File getAllocationFile() {
        return new File(FILE_PATH_PREFIX + "default.allocation");
    }

    @Override
    protected File getResourceEnvironmentFile() {
        return new File(FILE_PATH_PREFIX + "My.resourceenvironment");
    }
}
