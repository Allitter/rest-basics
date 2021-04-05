package com.epam.esm.repository.specification;

import com.epam.esm.model.Certificate;

public class CertificateByDescriptionSpecification implements Specification<Certificate> {
    private final String description;

    public CertificateByDescriptionSpecification(String description) {
        this.description = description;
    }

    @Override
    public String query() {
        return "select * from certificate where description like ?";
    }

    @Override
    public Object[] getArgs() {
        return new Object[]{"%" + description + "%"};
    }
}
