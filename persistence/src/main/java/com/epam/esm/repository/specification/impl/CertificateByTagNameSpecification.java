package com.epam.esm.repository.specification.impl;

import com.epam.esm.repository.specification.AbstractCertificateSpecification;

public class CertificateByTagNameSpecification extends AbstractCertificateSpecification {
    private final String tagName;

    public CertificateByTagNameSpecification(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public String getCondition() {
        return "id in (select id_certificate from certificate_tag where id_tag in (select id from tag where name like ?))";
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{"%" + tagName + "%"};
    }
}
