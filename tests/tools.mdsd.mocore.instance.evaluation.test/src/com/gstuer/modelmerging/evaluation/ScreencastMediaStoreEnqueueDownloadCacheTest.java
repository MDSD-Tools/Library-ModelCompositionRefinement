package com.gstuer.modelmerging.evaluation;

import java.io.File;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ScreencastMediaStoreEnqueueDownloadCacheTest extends CaseStudyTest {
    private static final String FILE_PATH_PREFIX = "./resources/evaluation/case-studies/palladio-example-models/"
            + "ScreencastMediaStore/";

    @Override
    @ParameterizedTest
    @ValueSource(strings = { "_j5vZQLhEEeSNPorBlo7x9g", "_qxAiILg7EeSNPorBlo7x9g", "_p_EeYHDrEeSqnN80MQ2uGw",
            "_B5geQHDsEeSqnN80MQ2uGw", "_9eK7YHDrEeSqnN80MQ2uGw" })
    public void testRefinementComponentWithoutContainer(String componentIdentifier) {
        super.testRefinementComponentWithoutContainer(componentIdentifier);
    }

    @Override
    @ParameterizedTest
    @ValueSource(strings = { "_jf7hMLg7EeSNPorBlo7x9g", "_4QwQwHDqEeSqnN80MQ2uGw", "_psfEIHDsEeSqnN80MQ2uGw",
            "_s3KlgHDuEeSqnN80MQ2uGw", "_Nv-1oHDvEeSqnN80MQ2uGw" })
    public void testRefinementInterfaceWithoutComponent(String interfaceIdentifier) {
        super.testRefinementInterfaceWithoutComponent(interfaceIdentifier);
    }

    @Override
    @ParameterizedTest
    @ValueSource(strings = { "_wBCOILg3EeSNPorBlo7x9g" })
    public void testRefinementComponentAssemblyRequiresContainerLink(String linkIdentifier) {
        super.testRefinementComponentAssemblyRequiresContainerLink(linkIdentifier);
    }

    @Override
    @ParameterizedTest
    @ValueSource(strings = { "_wBCOILg3EeSNPorBlo7x9g" })
    public void testRefinementContainerLinkNotReplacedIndirectly(String linkIdentifier) {
        super.testRefinementContainerLinkNotReplacedIndirectly(linkIdentifier);
    }

    @Override
    protected String getCaseStudyName() {
        return "Screencast MediaStore Enqueue";
    }

    @Override
    protected File getRepositoryFile() {
        return new File(FILE_PATH_PREFIX + "MediaStore.repository");
    }

    @Override
    protected File getSystemFile() {
        return new File(FILE_PATH_PREFIX + "MediaStore-EnqueueDownloadCache.system");
    }

    @Override
    protected File getAllocationFile() {
        return new File(FILE_PATH_PREFIX + "MediaStore-EnqueueDownloadCache.allocation");
    }

    @Override
    protected File getResourceEnvironmentFile() {
        return new File(FILE_PATH_PREFIX + "MediaStore.resourceenvironment");
    }
}
