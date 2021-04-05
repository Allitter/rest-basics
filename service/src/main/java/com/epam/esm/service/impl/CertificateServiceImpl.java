package com.epam.esm.service.impl;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.repository.specification.*;
import com.epam.esm.service.CertificateQueryObject;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.stream.CertificateStream;
import com.epam.esm.service.stream.impl.CertificateStreamImpl;
import com.epam.esm.validator.CertificateValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static java.util.Objects.nonNull;

@Service
public class CertificateServiceImpl implements CertificateService {
    private static final String ASC_SORT = "asc";
    private final MainRepository<Tag> tagRepository;
    private final MainRepository<Certificate> repository;
    private final CertificateValidator validator;

    public CertificateServiceImpl(MainRepository<Certificate> repository,
                                  MainRepository<Tag> tagRepository,
                                  CertificateValidator validator) {
        this.repository = repository;
        this.tagRepository = tagRepository;
        this.validator = validator;
    }

    @Override
    public Certificate findById(int id) {
        Optional<Certificate> optionalCertificate = repository.queryFirst(new CertificateByIdSpecification(id));
        return optionalCertificate.orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public CertificateStream findAll() {
        List<Certificate> certificates = repository.query(new CertificateAllSpecification());
        return new CertificateStreamImpl(certificates);
    }

    @Override
    public CertificateStream findByTagName(String tagName) {
        List<Certificate> certificates = repository.query(new CertificateByTagNameSpecification(tagName));
        return new CertificateStreamImpl(certificates);
    }

    @Override
    public CertificateStream findByName(String name) {
        List<Certificate> certificates = repository.query(new CertificateByNameSpecification(name));
        return new CertificateStreamImpl(certificates);
    }

    @Override
    public CertificateStream findByDescription(String description) {
        List<Certificate> certificates = repository.query(new CertificateByDescriptionSpecification(description));
        return new CertificateStreamImpl(certificates);
    }

    @Override
    @Transactional
    public Certificate update(Certificate certificate) {
        Map<String, String> validations = validator.validateForUpdate(certificate);
        if (!validations.isEmpty()) {
            throw new ValidationException(validations);
        }

        Optional<Certificate> optional = repository.queryFirst(new CertificateByIdSpecification(certificate.getId()));
        Certificate oldCertificate = optional.orElseThrow(EntityNotFoundException::new);

        Certificate updatedCertificate = Certificate.Builder.merge(oldCertificate, certificate);
        List<Tag> tags = updatedCertificate.getTags();
        List<Tag> ensuredTags = ensureTagsInRepo(tags);

        updatedCertificate = new Certificate.Builder(updatedCertificate)
                .setLastUpdateDate(LocalDate.now())
                .setTags(ensuredTags)
                .build();

        updatedCertificate = repository.update(updatedCertificate);
        return updatedCertificate;
    }

    @Override
    @Transactional
    public Certificate add(Certificate certificate) {
        Map<String, String> validations = validator.validateForCreate(certificate);
        if (!validations.isEmpty()) {
            throw new ValidationException(validations);
        }

        List<Tag> tags = certificate.getTags() != null
                ? ensureTagsInRepo(certificate.getTags())
                : Collections.emptyList();

        certificate = new Certificate.Builder(certificate)
                .setCreateDate(LocalDate.now())
                .setLastUpdateDate(LocalDate.now())
                .setTags(tags).build();

        certificate = repository.add(certificate);
        return certificate;
    }

    @Override
    public boolean remove(int id) {
        return repository.remove(id);
    }

    private List<Tag> ensureTagsInRepo(List<Tag> tags) {
        for (int i = 0; i < tags.size(); i++) {
            Tag tag = tags.get(i);
            Specification<Tag> specification = new TagByNameSpecification(tag.getName());
            Optional<Tag> optional = tagRepository.queryFirst(specification);
            tag = optional.isPresent() ? optional.get() : tagRepository.add(tag);
            tags.set(i, tag);
        }

        return tags;
    }

    @Override
    public List<Certificate> findCertificatesByQueryObject(CertificateQueryObject queryObject) {
        CertificateStream stream = null;
        if (nonNull(queryObject.getName())) {
            stream = findByName(queryObject.getName());
        }

        if (nonNull(queryObject.getDescription())) {
            String description = queryObject.getDescription();
            stream = stream == null ? findByDescription(description) : stream.descriptionLike(description);
        }

        if (nonNull(queryObject.getTagName())) {
            String tagName = queryObject.getTagName();
            stream = stream == null ? findByTagName(tagName) : stream.tagNameLike(tagName);
        }

        stream = stream == null ? findAll() : stream;

        if (nonNull(queryObject.getSortDate())) {
            boolean asc = queryObject.getSortDate().trim().toLowerCase(Locale.ROOT).contains(ASC_SORT);
            stream.sortCreateDate(asc);
        }

        if (nonNull(queryObject.getSortName())) {
            boolean asc = queryObject.getSortName().trim().toLowerCase(Locale.ROOT).contains(ASC_SORT);
            stream.sortName(asc);
        }

        return stream.get();
    }
}
