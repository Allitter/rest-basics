package com.epam.esm.validator;

import com.epam.esm.model.Certificate;

import java.util.Map;

/**
 * Certificate validator.
 */
public interface CertificateValidator {
    /**
     * Validate for create. Validates all certificate fields
     *
     * @param certificate the certificate to validate
     * @return the map of fails (attribute; fail)
     */
    Map<String, String> validateForCreate(Certificate certificate);

    /**
     * Validate for update map. Validates only non null fields
     *
     * @param certificate the certificate to validate
     * @return the map of fails (attribute; fail)
     */
    Map<String, String> validateForUpdate(Certificate certificate);
}
