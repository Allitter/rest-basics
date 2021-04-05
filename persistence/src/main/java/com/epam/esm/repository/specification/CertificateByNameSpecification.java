package com.epam.esm.repository.specification;

import com.epam.esm.model.Certificate;

public class CertificateByNameSpecification implements Specification<Certificate> {
    private final String name;

    public CertificateByNameSpecification(String name) {
        this.name = name;
    }

    @Override
    public String query() {
        return "select * from certificate where name like ?";
    }

    @Override
    public Object[] getArgs() {
        return new Object[]{"%" + name + "%"};
    }
}
