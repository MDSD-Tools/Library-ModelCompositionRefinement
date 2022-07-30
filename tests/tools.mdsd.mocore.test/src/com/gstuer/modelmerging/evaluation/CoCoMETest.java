package com.gstuer.modelmerging.evaluation;

import java.io.File;

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

public class CoCoMETest extends CaseStudyTest {
    private static final String FILE_PATH_PREFIX = "./resources/evaluation/case-studies/cocome/PCM4.0/";

    @Override
    protected Repository getRepository() {
        File file = new File(FILE_PATH_PREFIX + "cocome.repository");
        return loadRepositoryFromFile(file);
    }

    @Override
    protected System getSystem() {
        File file = new File(FILE_PATH_PREFIX + "cocome.system");
        return loadSystemFromFile(file);
    }

    @Override
    protected Allocation getAllocation() {
        File file = new File(FILE_PATH_PREFIX + "cocome.allocation");
        return loadAllocationFromFile(file);
    }

    @Override
    protected ResourceEnvironment getResourceEnvironment() {
        File file = new File(FILE_PATH_PREFIX + "cocome.resourceenvironment");
        return loadResourceEnvironmentFromFile(file);
    }
}
