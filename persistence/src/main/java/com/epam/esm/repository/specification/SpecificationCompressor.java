package com.epam.esm.repository.specification;

import com.epam.esm.model.Entity;

import java.util.List;

public interface SpecificationCompressor<T extends Entity> {

    Specification<T> buildFrom(List<Specification<T>> specifications);
}
