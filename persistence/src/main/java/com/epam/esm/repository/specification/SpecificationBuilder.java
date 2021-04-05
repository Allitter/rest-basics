package com.epam.esm.repository.specification;

import com.epam.esm.model.Entity;
import com.epam.esm.repository.specification.impl.GenericSpecification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SpecificationBuilder<T extends Entity> {
    private static final String APPEND_CONDITION = "%s AND %s";
    private String query;
    private final List<Object> args;

    public SpecificationBuilder(Specification<T> specification) {
        query = specification.query();
        args = new ArrayList<>(Arrays.asList(specification.getArgs()));
    }

    public SpecificationBuilder<T> append(Specification<T> specification) {
        query = String.format(APPEND_CONDITION, query, specification.getCondition());
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
