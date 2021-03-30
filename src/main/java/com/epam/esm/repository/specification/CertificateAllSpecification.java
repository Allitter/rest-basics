package com.epam.esm.repository.specification;

import com.epam.esm.model.Certificate;

public class CertificateAllSpecification implements Specification<Certificate> {

    @Override
    public String query() {
        return "SELECT * FROM certificate;";
    }
}
