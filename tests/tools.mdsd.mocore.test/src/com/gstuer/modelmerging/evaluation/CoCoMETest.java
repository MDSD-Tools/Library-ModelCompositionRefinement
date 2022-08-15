package com.gstuer.modelmerging.evaluation;

import java.io.File;

public class CoCoMETest extends CaseStudyTest {
    private static final String FILE_PATH_PREFIX = "./resources/evaluation/case-studies/cocome/PCM4.0/";

    @Override
    protected File getRepositoryFile() {
        return new File(FILE_PATH_PREFIX + "cocome.repository");
    }

    @Override
    protected File getSystemFile() {
        return new File(FILE_PATH_PREFIX + "cocome.system");
    }

    @Override
    protected File getAllocationFile() {
        return new File(FILE_PATH_PREFIX + "cocome.allocation");
    }

    @Override
    protected File getResourceEnvironmentFile() {
        return new File(FILE_PATH_PREFIX + "cocome.resourceenvironment");
    }
}
