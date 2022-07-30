package com.gstuer.modelmerging.evaluation;

import java.io.File;

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

public class ScreencastMediaStoreCachelessTest extends CaseStudyTest {
    private static final String FILE_PATH_PREFIX = "./resources/evaluation/case-studies/palladio-example-models/"
            + "ScreencastMediaStore/";

    @Override
    protected Repository getRepository() {
        File file = new File(FILE_PATH_PREFIX + "MediaStore.repository");
        return loadRepositoryFromFile(file);
    }

    @Override
    protected System getSystem() {
        File file = new File(FILE_PATH_PREFIX + "MediaStore-Cacheless.system");
        return loadSystemFromFile(file);
    }

    @Override
    protected Allocation getAllocation() {
        File file = new File(FILE_PATH_PREFIX + "MediaStore-Cacheless.allocation");
        return loadAllocationFromFile(file);
    }

    @Override
    protected ResourceEnvironment getResourceEnvironment() {
        File file = new File(FILE_PATH_PREFIX + "MediaStore.resourceenvironment");
        return loadResourceEnvironmentFromFile(file);
    }
}
