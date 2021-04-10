package com.epam.esm.repository.specification;

import com.epam.esm.model.Certificate;

public abstract class AbstractCertificateSpecification extends AbstractSpecification<Certificate> {
    private static final String TABLE_NAME = "certificate";

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

}
