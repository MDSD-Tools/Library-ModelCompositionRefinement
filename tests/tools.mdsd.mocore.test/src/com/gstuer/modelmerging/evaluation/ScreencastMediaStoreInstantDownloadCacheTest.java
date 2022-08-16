package com.gstuer.modelmerging.evaluation;

import java.io.File;

public class ScreencastMediaStoreInstantDownloadCacheTest extends CaseStudyTest {
    private static final String FILE_PATH_PREFIX = "./resources/evaluation/case-studies/palladio-example-models/"
            + "ScreencastMediaStore/";

    @Override
    protected File getRepositoryFile() {
        return new File(FILE_PATH_PREFIX + "MediaStore.repository");
    }

    @Override
    protected File getSystemFile() {
        return new File(FILE_PATH_PREFIX + "MediaStore-InstantDownloadCache.system");
    }

    @Override
    protected File getAllocationFile() {
        return new File(FILE_PATH_PREFIX + "MediaStore-InstantDownloadCache.allocation");
    }

    @Override
    protected File getResourceEnvironmentFile() {
        return new File(FILE_PATH_PREFIX + "MediaStore.resourceenvironment");
    }
}
