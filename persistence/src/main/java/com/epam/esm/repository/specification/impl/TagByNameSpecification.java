package com.epam.esm.repository.specification.impl;

import com.epam.esm.repository.specification.AbstractTagSpecification;

public class TagByNameSpecification extends AbstractTagSpecification {
    private final String name;

    public TagByNameSpecification(String name) {
        this.name = name;
    }

    @Override
    public String getCondition() {
        return "name = ?";
    }

    @Override
    public Object[] getArgs() {
        return new Object[]{name};
    }
}
