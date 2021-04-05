package com.epam.esm.repository.specification.impl;

import com.epam.esm.repository.specification.AbstractCertificateSpecification;

public class CertificateByNameSpecification extends AbstractCertificateSpecification {
    private final String name;

    public CertificateByNameSpecification(String name) {
        this.name = name;
    }

    @Override
    public String getCondition() {
        return "name like ?";
    }

    @Override
    public Object[] getArgs() {
        return new Object[]{"%" + name + "%"};
    }
}
