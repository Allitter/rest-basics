package com.epam.esm.service.impl;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.ValidationError;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.repository.specification.Specification;
import com.epam.esm.repository.specification.impl.*;
import com.epam.esm.service.CertificateQueryObject;
import com.epam.esm.service.CertificateService;
import com.epam.esm.validator.CertificateValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public List<Certificate> findAll() {
        return repository.query(new CertificateAllSpecification());
    }

    @Override
    public List<Certificate> findByTagName(String tagName) {
        return repository.query(new CertificateByTagNameSpecification(tagName));
    }

    @Override
    public List<Certificate> findByName(String name) {
        return repository.query(new CertificateByNameSpecification(name));
    }

    @Override
    public List<Certificate> findByDescription(String description) {
        return repository.query(new CertificateByDescriptionSpecification(description));
    }

    @Override
    @Transactional
    public Certificate update(Certificate certificate) {
        validateForUpdate(certificate);

        Optional<Certificate> optional = repository.queryFirst(new CertificateByIdSpecification(certificate.getId()));
        Certificate oldCertificate = optional.orElseThrow(EntityNotFoundException::new);

        Certificate certificateToUpdate = Certificate.Builder.merge(oldCertificate, certificate);
        List<Tag> tags = new ArrayList<>(certificateToUpdate.getTags());
        List<Tag> ensuredTags = ensureTagsInRepo(tags);

        certificateToUpdate = new Certificate.Builder(certificateToUpdate)
                .setLastUpdateDate(LocalDate.now())
                .setTags(ensuredTags)
                .build();

        return repository.update(certificateToUpdate);
    }

    private void validateForUpdate(Certificate certificate) {
        Map<String, String> validations = validator.validateForUpdate(certificate);
        if (!validations.isEmpty()) {
            throw new ValidationError(validations);
        }
    }

    @Override
    @Transactional
    public Certificate add(Certificate certificate) {
        validateForCreate(certificate);

        List<Tag> tags = certificate.getTags() != null
                ? ensureTagsInRepo(new ArrayList<>(certificate.getTags()))
                : Collections.emptyList();

        certificate = new Certificate.Builder(certificate)
                .setCreateDate(LocalDate.now())
                .setLastUpdateDate(LocalDate.now())
                .setTags(tags).build();

        certificate = repository.add(certificate);
        return certificate;
    }

    private void validateForCreate(Certificate certificate) {
        Map<String, String> validations = validator.validateForCreate(certificate);
        if (!validations.isEmpty()) {
            throw new ValidationError(validations);
        }
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
        List<Specification<Certificate>> specifications = new ArrayList<>();

        if (nonNull(queryObject.getName())) {
            specifications.add(new CertificateByNameSpecification(queryObject.getName()));
        }
        if (nonNull(queryObject.getDescription())) {
            specifications.add(new CertificateByDescriptionSpecification(queryObject.getDescription()));
        }
        if (nonNull(queryObject.getTagName())) {
            specifications.add(new CertificateByTagNameSpecification(queryObject.getTagName()));
        }

        Stream<Certificate> certificates = repository.query(specifications).stream();

        if (nonNull(queryObject.getSortDate())) {
            boolean asc = queryObject.getSortDate().trim().toLowerCase(Locale.ROOT).contains(ASC_SORT);
            certificates = asc
                    ? certificates.sorted(Comparator.comparing(Certificate::getCreateDate))
                    : certificates.sorted((first, second) -> second.getCreateDate().compareTo(first.getCreateDate()));
        }

        if (nonNull(queryObject.getSortName())) {
            boolean asc = queryObject.getSortName().trim().toLowerCase(Locale.ROOT).contains(ASC_SORT);
            certificates = asc
                    ? certificates.sorted(Comparator.comparing(Certificate::getName))
                    : certificates.sorted((first, second) -> second.getName().compareTo(first.getName()));
        }

        return certificates.collect(Collectors.toList());
    }
}
