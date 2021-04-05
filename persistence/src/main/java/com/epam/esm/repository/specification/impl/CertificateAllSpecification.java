package com.epam.esm.repository.specification.impl;

import com.epam.esm.repository.specification.AbstractCertificateSpecification;
import org.apache.commons.lang3.StringUtils;

public class CertificateAllSpecification extends AbstractCertificateSpecification {

    @Override
    public String getCondition() {
        return StringUtils.EMPTY;
    }
}
