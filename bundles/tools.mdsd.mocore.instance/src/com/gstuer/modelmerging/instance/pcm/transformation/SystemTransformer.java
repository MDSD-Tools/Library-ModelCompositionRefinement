package com.gstuer.modelmerging.instance.pcm.transformation;

import org.palladiosimulator.generator.fluent.system.api.ISystemAddition;
import org.palladiosimulator.generator.fluent.system.factory.FluentSystemFactory;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.system.System;

import com.gstuer.modelmerging.framework.transformation.Transformer;
import com.gstuer.modelmerging.instance.pcm.surrogate.PcmSurrogate;

public class SystemTransformer implements Transformer<PcmSurrogate, org.palladiosimulator.pcm.system.System> {
    @Override
    public System transform(PcmSurrogate model) {
        FluentSystemFactory systemFactory = new FluentSystemFactory();
        Repository repository = new RepositoryTransformer().transform(model);
        ISystemAddition fluentSystem = systemFactory.newSystem().addRepository(repository);

        // TODO Add repository components as assembly contexts

        // TODO Add assembly connectors (component assembly relations)

        return fluentSystem.createSystemNow();
    }
}
