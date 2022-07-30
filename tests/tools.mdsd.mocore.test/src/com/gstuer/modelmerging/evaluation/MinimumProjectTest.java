package com.gstuer.modelmerging.evaluation;

import java.io.File;

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

public class MinimumProjectTest extends CaseStudyTest {
    private static final String FILE_PATH_PREFIX = "./resources/evaluation/case-studies/palladio-example-models/"
            + "Minimum_Project_Example/";

    @Override
    protected Repository getRepository() {
        File file = new File(FILE_PATH_PREFIX + "default.repository");
        return loadRepositoryFromFile(file);
    }

    @Override
    protected System getSystem() {
        File file = new File(FILE_PATH_PREFIX + "default.system");
        return loadSystemFromFile(file);
    }

    @Override
    protected Allocation getAllocation() {
        File file = new File(FILE_PATH_PREFIX + "default.allocation");
        return loadAllocationFromFile(file);
    }

    @Override
    protected ResourceEnvironment getResourceEnvironment() {
        File file = new File(FILE_PATH_PREFIX + "My.resourceenvironment");
        return loadResourceEnvironmentFromFile(file);
    }
}
