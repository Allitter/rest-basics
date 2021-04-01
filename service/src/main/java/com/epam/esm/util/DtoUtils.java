package com.epam.esm.util;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.model.Certificate;

public final class DtoUtils {

    public static CertificateDto certificateToDto(Certificate certificate) {
        return new CertificateDto(
                certificate.getId(),
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration(),
                certificate.getCreateDate(),
                certificate.getLastUpdateDate(),
                certificate.getTags());
    }

    public static Certificate dtoToCertificate(CertificateDto dto) {
        return new Certificate.Builder()
                .setId(dto.getId())
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setPrice(dto.getPrice())
                .setDuration(dto.getDuration())
                .setCreateDate(dto.getCreateDate())
                .setLastUpdateDate(dto.getLastUpdateDate())
                .setTags(dto.getTags())
                .build();
    }

    private DtoUtils(){
    }
}
