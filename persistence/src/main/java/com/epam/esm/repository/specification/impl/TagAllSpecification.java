package com.epam.esm.repository.specification.impl;

import com.epam.esm.repository.specification.AbstractTagSpecification;
import org.apache.commons.lang3.StringUtils;

public class TagAllSpecification extends AbstractTagSpecification {

    @Override
    public String getCondition() {
        return StringUtils.EMPTY;
    }
}
