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
        return new File(FILE_PATH_PREFIX + "minimalProject.repository");
    }

    @Override
    protected File getSystemFile() {
        return new File(FILE_PATH_PREFIX + "minimalProject.system");
    }

    @Override
    protected File getAllocationFile() {
        return new File(FILE_PATH_PREFIX + "minimalProject.allocation");
    }

    @Override
    protected File getResourceEnvironmentFile() {
        return new File(FILE_PATH_PREFIX + "minimalProject.resourceenvironment");
    }
}
