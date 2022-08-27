package com.gstuer.modelmerging.evaluation;

import java.io.File;

public class CoReXTest extends CaseStudyTest {
    private static final String FILE_PATH_PREFIX = "./resources/evaluation/case-studies/CoReX/";

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
