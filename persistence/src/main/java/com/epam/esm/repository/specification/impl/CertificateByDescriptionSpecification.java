package com.epam.esm.repository.specification.impl;

import com.epam.esm.repository.specification.AbstractCertificateSpecification;

public class CertificateByDescriptionSpecification extends AbstractCertificateSpecification {
    private final String description;

    public CertificateByDescriptionSpecification(String description) {
        this.description = description;
    }

    @Override
    public String getCondition() {
        return "description like ?";
    }

    @Override
    public Object[] getArgs() {
        return new Object[]{"%" + description + "%"};
    }
}
