package com.epam.esm.validation;

import com.epam.esm.dto.CertificateDto;

import java.util.Map;

public interface CertificateDtoValidator {
    Map<String, String> validateForCreate(CertificateDto dto);

    Map<String, String> validateForUpdate(CertificateDto dto);
}
