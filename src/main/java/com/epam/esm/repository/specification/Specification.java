package com.epam.esm.repository.specification;

import com.epam.esm.model.Entity;

public interface Specification<T extends Entity> {
    String query();

    default Object[] getArgs() {
        return new Object[0];
    }
}
