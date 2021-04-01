package com.epam.esm.service.stream.impl;

import com.epam.esm.controller.dto.CertificateDto;
import com.epam.esm.model.Certificate;
import com.epam.esm.service.stream.CertificateStream;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CertificateStreamImpl implements CertificateStream {
    private Stream<Certificate> certificateStream;

    public CertificateStreamImpl(List<Certificate> certificates) {
        this.certificateStream = certificates.stream();
    }

    @Override
    public CertificateStream nameLike(String name) {
        certificateStream = certificateStream.filter(certificate -> certificate.getName().contains(name));
        return this;
    }

    @Override
    public CertificateStream descriptionLike(String description) {
        certificateStream = certificateStream.filter(certificate -> certificate.getDescription().contains(description));
        return this;
    }

    @Override
    public CertificateStream tagNameLike(String tagName) {
        certificateStream = certificateStream
                .filter(certificate -> certificate.getTags().stream()
                        .anyMatch(t -> t.getName().contains(tagName)));

        return this;
    }

    @Override
    public CertificateStream sortName(boolean asc) {
        certificateStream = asc
                ? certificateStream.sorted(Comparator.comparing(Certificate::getName))
                : certificateStream.sorted((a, b) -> b.getName().compareTo(a.getName()));

        return this;
    }

    @Override
    public CertificateStream sortCreateDate(boolean asc) {
        certificateStream = asc
                ? certificateStream.sorted(Comparator.comparing(Certificate::getCreateDate))
                : certificateStream.sorted((a, b) -> b.getCreateDate().compareTo(a.getCreateDate()));

        return this;
    }

    @Override
    public List<CertificateDto> get() {
        return certificateStream.map(CertificateDto::new).collect(Collectors.toList());
    }
}
