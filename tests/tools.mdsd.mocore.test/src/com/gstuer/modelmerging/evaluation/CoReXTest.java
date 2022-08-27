package com.gstuer.modelmerging.evaluation;

import java.io.File;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CoReXTest extends CaseStudyTest {
    private static final String FILE_PATH_PREFIX = "./resources/evaluation/case-studies/CoReX/";

    // region Refinement Evaluation

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
