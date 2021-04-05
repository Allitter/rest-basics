package com.epam.esm.repository.specification.impl;

import com.epam.esm.repository.specification.AbstractCertificateSpecification;

public class CertificateByIdSpecification extends AbstractCertificateSpecification {
    private final long id;

    public CertificateByIdSpecification(long id) {
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
