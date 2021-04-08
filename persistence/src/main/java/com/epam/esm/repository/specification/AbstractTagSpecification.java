package com.epam.esm.repository.specification;

import com.epam.esm.model.Tag;

public abstract class AbstractTagSpecification extends AbstractSpecification<Tag> {
    private static final String TABLE_NAME = "tag";

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }
}
