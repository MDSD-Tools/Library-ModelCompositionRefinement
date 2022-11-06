package com.gstuer.modelmerging.framework.merger;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.gstuer.modelmerging.framework.surrogate.Model;
import com.gstuer.modelmerging.framework.surrogate.Relation;
import com.gstuer.modelmerging.framework.surrogate.Replaceable;

public abstract class RelationProcessor<M extends Model, T extends Relation<?, ?>> extends Processor<M, T> {
    protected RelationProcessor(M model, Class<T> processableType) {
        super(model, processableType);
    }

    @Override
    protected void refine(T discovery) {
        // Add trivial implications for source and destination of relation
        addImplication(discovery.getSource());
        addImplication(discovery.getDestination());

        // Replace indirect and direct placeholder relations of the merged relation
        replaceDirectPlaceholders(discovery);
        replaceIndirectPlaceholders(discovery);
    }

    protected void replaceDirectPlaceholders(T discovery) {
        // Get relevant placeholder relations from the model
        List<T> relations = this.getModel().getByType(this.getProcessableType());
        relations.removeIf(relation -> !relation.isPlaceholderOf(discovery) && !discovery.isPlaceholderOf(relation));

        Set<Replaceable> implications = new HashSet<>();
        for (T relation : relations) {
            // Replace placeholder & collect possible implications. May include discovery.
            if (relation.isPlaceholderOf(discovery)) {
                implications.addAll(this.getModel().replace(relation, discovery));
                this.replaceImplications(relation, discovery);
            } else if (discovery.isPlaceholderOf(relation)) {
                implications.addAll(this.getModel().replace(discovery, relation));
                this.replaceImplications(discovery, relation);
            }
        }
        // The discovery was already added to the model by the merge operation.
        implications.remove(discovery);
        this.addImplications(implications);
    }

    protected void replaceIndirectPlaceholders(T discovery) {
        // Get relations of same type with equal source or destination
        Set<T> relations = this.getModel().getByType(this.getProcessableType()).stream()
                .filter(relation -> !Objects.equals(relation, discovery))
                .filter(relation -> !relation.isPlaceholderOf(discovery))
                .filter(relation -> !discovery.isPlaceholderOf(relation))
                .filter(relation -> (discovery.getSource().equals(relation.getSource())
                        || discovery.getDestination().equals(relation.getDestination())))
                .collect(Collectors.toSet());

        Set<Replaceable> implications = new HashSet<>();
        for (T relation : relations) {
            /*
             * For each possible placeholder relation identify the common side with the discovery relation. Then check
             * whether the uncommon side of a possible placeholder is a placeholder itself. If yes, the possible
             * placeholder is a certain placeholder and replacement of the uncommon side is needed. Moreover,
             * replacement of the whole relation is needed if the discovery relation is not a placeholder. Afterwards
             * replace the uncommon side of the placeholder relation with the uncommon side of the discovery. This may
             * be a renaming without information benefit if both uncommon sides are placeholders.
             */
            if (Objects.equals(discovery.getSource(), relation.getSource())) {
                // Left side equals -> Right side replacement if needed
                if (relation.getDestination().isPlaceholder()) {
                    if (!discovery.isPlaceholder()) {
                        implications.addAll(this.getModel().replace(relation, discovery));
                    }
                    implications.addAll(this.getModel()
                            .replace(relation.getDestination(), discovery.getDestination()));
                } else if (discovery.getDestination().isPlaceholder()) {
                    implications.addAll(this.getModel()
                            .replace(discovery.getDestination(), relation.getDestination()));
                    this.removeImplication(discovery.getDestination());
                }
            } else if (Objects.equals(discovery.getDestination(), relation.getDestination())) {
                // Right side equals -> Left side replacement if needed
                if (relation.getSource().isPlaceholder()) {
                    if (!discovery.isPlaceholder()) {
                        implications.addAll(this.getModel().replace(relation, discovery));
                    }
                    implications.addAll(this.getModel()
                            .replace(relation.getSource(), discovery.getSource()));
                } else if (discovery.getSource().isPlaceholder()) {
                    implications.addAll(this.getModel()
                            .replace(discovery.getSource(), relation.getSource()));
                    this.removeImplication(discovery.getSource());
                }
            }
        }
        // The discovery was already added to the model by the merge operation.
        implications.remove(discovery);
        this.addImplications(implications);
    }
}
