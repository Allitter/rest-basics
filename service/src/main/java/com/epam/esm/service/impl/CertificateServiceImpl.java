package com.epam.esm.service.impl;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.repository.specification.Specification;
import com.epam.esm.repository.specification.impl.*;
import com.epam.esm.service.CertificateQueryObject;
import com.epam.esm.service.CertificateService;
import com.epam.esm.validator.CertificateValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

@Service
public class CertificateServiceImpl implements CertificateService {
    private static final String ASCENDING_SORT = "asc";
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
    public Certificate findById(long id) {
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
        throwExceptionIfNotValid(validator.validateForUpdate(certificate));

        Optional<Certificate> optional = repository.queryFirst(new CertificateByIdSpecification(certificate.getId()));
        Certificate oldCertificate = optional.orElseThrow(EntityNotFoundException::new);

        Certificate certificateToUpdate = Certificate.Builder.merge(oldCertificate, certificate);
        List<Tag> ensuredTags = ensureTagsInRepo(certificateToUpdate.getTags());

        certificateToUpdate = new Certificate.Builder(certificateToUpdate)
                .setLastUpdateDate(LocalDate.now())
                .setTags(ensuredTags)
                .build();

        return repository.update(certificateToUpdate);
    }

    @Override
    @Transactional
    public Certificate add(Certificate certificate) {
        throwExceptionIfNotValid(validator.validateForCreate(certificate));

        List<Tag> tags = CollectionUtils.isNotEmpty(certificate.getTags())
                ? ensureTagsInRepo(certificate.getTags())
                : Collections.emptyList();

        certificate = new Certificate.Builder(certificate)
                .setCreateDate(LocalDate.now())
                .setLastUpdateDate(LocalDate.now())
                .setTags(tags).build();

        return repository.add(certificate);
    }

    private void throwExceptionIfNotValid(Map<String, String> validationFails) {
        if (MapUtils.isNotEmpty(validationFails)) {
            throw new ValidationException(validationFails);
        }
    }

    @Override
    public boolean remove(long id) {
        return repository.remove(id);
    }

    private List<Tag> ensureTagsInRepo(List<Tag> tags) {
        List<Tag> tagsCopy = new ArrayList<>(tags);

        for (int i = 0; i < tags.size(); i++) {
            Tag tag = tagsCopy.get(i);
            Specification<Tag> specification = new TagByNameSpecification(tag.getName());
            Optional<Tag> optional = tagRepository.queryFirst(specification);
            tag = optional.isPresent() ? optional.get() : tagRepository.add(tag);
            tagsCopy.set(i, tag);
        }

        return tagsCopy;
    }

    @Override
    public List<Certificate> findCertificatesByQueryObject(CertificateQueryObject queryObject) {
        List<Specification<Certificate>> specifications = new ArrayList<>();

        if (StringUtils.isNotBlank(queryObject.getName())) {
            specifications.add(new CertificateByNameSpecification(queryObject.getName()));
        }
        if (StringUtils.isNotBlank(queryObject.getDescription())) {
            specifications.add(new CertificateByDescriptionSpecification(queryObject.getDescription()));
        }
        if (StringUtils.isNotBlank(queryObject.getTagName())) {
            specifications.add(new CertificateByTagNameSpecification(queryObject.getTagName()));
        }
        if (specifications.isEmpty()) {
            specifications.add(new CertificateAllSpecification());
        }

        List<Certificate> certificates = repository.query(specifications);
        Stream<Certificate> certificateStream = certificates.stream();

        if (nonNull(queryObject.getSortDate())) {
            boolean isAscending = StringUtils.containsIgnoreCase(queryObject.getSortDate(), ASCENDING_SORT);
            certificateStream = isAscending
                    ? certificateStream.sorted(Comparator.comparing(Certificate::getCreateDate))
                    : certificateStream.sorted((first, second) -> second.getCreateDate().compareTo(first.getCreateDate()));
        }

        if (StringUtils.isNotBlank(queryObject.getSortName())) {
            boolean isAscending = StringUtils.containsIgnoreCase(queryObject.getSortName(), ASCENDING_SORT);
            certificateStream = isAscending
                    ? certificateStream.sorted(Comparator.comparing(Certificate::getName))
                    : certificateStream.sorted((first, second) -> second.getName().compareTo(first.getName()));
        }

        return certificateStream.collect(Collectors.toList());
    }
}
