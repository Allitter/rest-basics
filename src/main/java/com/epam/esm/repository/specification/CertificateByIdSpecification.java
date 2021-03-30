package com.epam.esm.repository.specification;

import com.epam.esm.model.Certificate;

public class CertificateByIdSpecification implements Specification<Certificate> {
    private final int id;

    public CertificateByIdSpecification(int id) {
        this.id = id;
    }

    @Override
    public String query() {
        return "SELECT * FROM certificate WHERE id = ?;";
    }

    @Override
    public Object[] getArgs() {
        return new Object[] { id };
    }
}
