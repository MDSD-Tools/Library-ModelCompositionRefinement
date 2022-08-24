package com.gstuer.modelmerging.evaluation;

import java.io.File;

public class MediaStoreBaseTest extends CaseStudyTest {
    private static final String FILE_PATH_PREFIX = "./resources/evaluation/case-studies/media-store-3/";

    @Override
    protected String getCaseStudyName() {
        return "MediaStore";
    }

    @Override
    protected File getRepositoryFile() {
        return new File(FILE_PATH_PREFIX + "ms.repository");
    }

    @Override
    protected File getSystemFile() {
        return new File(FILE_PATH_PREFIX + "ms_base.system");
    }

    @Override
    protected File getAllocationFile() {
        return new File(FILE_PATH_PREFIX + "ms_base.allocation");
    }

    @Override
    protected File getResourceEnvironmentFile() {
        return new File(FILE_PATH_PREFIX + "ms.resourceenvironment");
    }
}
