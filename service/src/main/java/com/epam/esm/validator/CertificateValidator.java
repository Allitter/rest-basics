package com.epam.esm.validator;

import com.epam.esm.model.Certificate;

import java.util.Map;

public interface CertificateValidator {
    Map<String, String> validateForCreate(Certificate certificate);

    Map<String, String> validateForUpdate(Certificate certificate);
}
