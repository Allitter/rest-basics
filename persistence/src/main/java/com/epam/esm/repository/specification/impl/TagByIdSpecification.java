package com.epam.esm.repository.specification.impl;

import com.epam.esm.repository.specification.AbstractTagSpecification;

public class TagByIdSpecification extends AbstractTagSpecification {
    private final long id;

    public TagByIdSpecification(long id) {
        this.id = id;
    }

    @Override
    public String getCondition() {
        return "id = ?";
    }

    @Override
    public Object[] getArgs() {
        return new Object[]{id};
    }
}
