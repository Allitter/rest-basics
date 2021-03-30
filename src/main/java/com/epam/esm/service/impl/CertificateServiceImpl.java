package com.epam.esm.service.impl;

import com.epam.esm.controller.dto.CertificateDto;
import com.epam.esm.controller.dto.CertificateQueryObject;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.repository.specification.*;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.stream.CertificateStream;
import com.epam.esm.service.stream.impl.CertificateStreamImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
public class CertificateServiceImpl implements CertificateService {
    private static final String ASC_SORT = "asc";
    private final MainRepository<Tag> tagRepository;
    private final MainRepository<Certificate> repository;

    public CertificateServiceImpl(MainRepository<Certificate> repository, MainRepository<Tag> tagRepository) {
        this.repository = repository;
        this.tagRepository = tagRepository;
    }

    @Override
    public CertificateDto findById(int id) {
        Certificate certificate = repository
                .queryFirst(new CertificateByIdSpecification(id))
                .orElseThrow(EntityNotFoundException::new);
        return new CertificateDto(certificate);
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
    public CertificateDto update(CertificateDto dto) {
        Optional<Certificate> optionalOld = repository.queryFirst(new CertificateByIdSpecification(dto.getId()));
        if (optionalOld.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Certificate old = optionalOld.get();
        Certificate updated = Certificate.Builder.merge(old, dto);
        List<Tag> tags = updated.getTags();
        List<Tag> retrievedTags = retrieveTagsFromRepo(tags);

        updated = new Certificate.Builder(updated)
                .setLastUpdateDate(LocalDate.now())
                .setTags(retrievedTags)
                .build();

        updated = repository.update(updated);
        return new CertificateDto(updated);
    }

    @Override
    public CertificateDto add(CertificateDto dto) {
        Certificate certificate = new Certificate.Builder(dto).build();
        certificate = repository.add(certificate);
        return new CertificateDto(certificate);
    }

    @Override
    public boolean remove(int id) {
        return repository.remove(id);
    }

    private List<Tag> retrieveTagsFromRepo(List<Tag> tags) {
        for (int i = 0; i < tags.size(); i++) {
            Tag tag = tags.get(i);
            var specification = new TagByNameOrIdSpecification(tag.getName(), tag.getId());
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
            stream.sortName(asc);
        }

        if (nonNull(queryObject.getSortName())) {
            boolean asc = queryObject.getSortName().trim().toLowerCase(Locale.ROOT).contains(ASC_SORT);
            stream.sortCreateDate(asc);
        }

        return stream.get();
    }
}
