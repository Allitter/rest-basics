package com.epam.esm.repository.specification;

import com.epam.esm.model.Entity;
import com.epam.esm.repository.specification.impl.GenericSpecification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class AbstractSpecificationCompressor<T extends Entity> implements SpecificationCompressor<T> {
    private static final int FIRST = 0;

    @Override
    public Specification<T> buildFrom(List<Specification<T>> specifications) {
        SpecificationBuilder<T> builder = new SpecificationBuilder<>(specifications.get(FIRST));
        specifications.remove(FIRST);
        return builder.appendAll(specifications).build();
    }

    private static class SpecificationBuilder<T extends Entity> {
        private static final String APPEND_OPERATOR = "%s AND %s";
        private final List<Object> args;
        private String query;

        public SpecificationBuilder(Specification<T> specification) {
            query = specification.query();
            args = new ArrayList<>(Arrays.asList(specification.getArgs()));
        }

        public SpecificationBuilder<T> append(Specification<T> specification) {
            query = String.format(APPEND_OPERATOR, query, specification.getCondition());

            Collections.addAll(args, specification.getArgs());
            return this;
        }

        public SpecificationBuilder<T> appendAll(List<Specification<T>> specifications) {
            for (Specification<T> specification : specifications) {
                append(specification);
            }
            return this;
        }

        public Specification<T> build() {
            return new GenericSpecification(query, args);
        }
    }
}
