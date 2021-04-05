package com.epam.esm.repository.specification.impl;

import com.epam.esm.repository.specification.Specification;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class GenericSpecification implements Specification {
    private final String query;
    private final Object[] args;

    public GenericSpecification(String query, List<Object> args) {
        this.query = query;
        this.args = args.toArray();
    }

    @Override
    public String query() {
        return query;
    }

    @Override
    public String getCondition() {
        return StringUtils.EMPTY;
    }

    @Override
    public Object[] getArgs() {
        return args;
    }
}
