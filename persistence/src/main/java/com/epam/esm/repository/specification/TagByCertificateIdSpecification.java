package com.epam.esm.repository.specification;

import com.epam.esm.model.Tag;

public class TagByCertificateIdSpecification implements Specification<Tag> {
    private final int certificateId;

    public TagByCertificateIdSpecification(int certificateId) {
        this.certificateId = certificateId;
    }

    @Override
    public String query() {
        return "select t.id, name from tag as t join certificate_tag as ct on t.id = ct.id_tag where ct.id_certificate = ?";
    }

    @Override
    public Object[] getArgs() {
        return new Object[]{certificateId};
    }
}
