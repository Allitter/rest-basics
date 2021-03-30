package com.epam.esm.service.stream;

import java.util.List;
import java.util.Optional;

public interface ServiceStream<T> {

    List<T> get();

    default Optional<T> first() {
        List<T> certificates = get();
        return certificates.isEmpty() ? Optional.empty() : Optional.of(certificates.get(0));
    }

    default T firstOrDefault(T t) {
        return first().orElse(t);
    }
}
