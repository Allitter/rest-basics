package com.epam.esm.repository.specification;

import com.epam.esm.model.Entity;

import java.util.List;

/**
 * Specification compressor.
 *
 */
public interface SpecificationCompressor {

    /**
     * Build specification from list. Creates specification from another
     * specifications by connecting conditions with "AND" operator
     *
     * @param <T>            the specification type parameter
     * @param specifications the specifications
     * @return built specification
     */
    <T extends Entity> Specification<T> buildFrom(List<Specification<T>> specifications);
}
