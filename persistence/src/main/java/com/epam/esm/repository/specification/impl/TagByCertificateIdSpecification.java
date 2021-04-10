package com.epam.esm.repository.specification.impl;

import com.epam.esm.repository.specification.AbstractTagSpecification;

public class TagByCertificateIdSpecification extends AbstractTagSpecification {
    private final long certificateId;

    public TagByCertificateIdSpecification(long certificateId) {
        this.certificateId = certificateId;
    }

    @Override
    public String getCondition() {
        return "id in(select id_tag from certificate_tag where id_certificate = ?)";
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{certificateId};
    }
}
