package com.epam.esm.repository.specification;

import com.epam.esm.model.Certificate;

public class CertificateByTagNameSpecification implements Specification<Certificate> {
    private final String tagName;

    public CertificateByTagNameSpecification(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public String query() {
        return "select c.* from certificate as c join certificate_tag as ct on c.id = ct.id_certificate join tag as t on ct.id_tag = t.id where t.name LIKE ?";
    }

    @Override
    public Object[] getArgs() {
        return new Object[]{"%" + tagName + "%"};
    }
}
