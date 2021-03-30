package com.epam.esm.service;

import com.epam.esm.controller.dto.CertificateDto;
import com.epam.esm.controller.dto.CertificateQueryObject;
import com.epam.esm.model.Certificate;
import com.epam.esm.service.stream.CertificateStream;

import java.util.List;

public interface CertificateService {

    CertificateDto findById(int id);

    CertificateStream findAll();

    CertificateStream findByTagName(String tagName);

    CertificateStream findByName(String name);

    CertificateStream findByDescription(String description);

    CertificateDto update(CertificateDto dto);

    CertificateDto add(CertificateDto dto);

    boolean remove(int id);

    List<Certificate> findCertificatesByQueryObject(CertificateQueryObject queryObject);
}
