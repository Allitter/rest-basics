package com.epam.esm.service.stream;

import java.util.List;
import java.util.Optional;

/**
 * The Service stream interface.
 *
 * @param <T> the type of aggregated objects
 */
public interface ServiceStream<T> {

    /**
     * Get aggregated list.
     *
     * @return the list of aggregated objects
     */
    List<T> get();

    /**
     * Get first element
     *
     * @return the objects element or empty optional
     */
    default Optional<T> first() {
        List<T> certificates = get();
        return certificates.isEmpty() ? Optional.empty() : Optional.of(certificates.get(0));
    }

}
