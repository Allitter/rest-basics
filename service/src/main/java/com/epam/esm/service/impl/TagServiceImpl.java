package com.epam.esm.service.impl;

import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.repository.specification.impl.TagAllSpecification;
import com.epam.esm.repository.specification.impl.TagByIdSpecification;
import com.epam.esm.repository.specification.impl.TagByNameSpecification;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.TagValidator;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private final MainRepository<Tag> repository;
    private final TagValidator tagValidator;

    public TagServiceImpl(MainRepository<Tag> repository, TagValidator tagValidator) {
        this.repository = repository;
        this.tagValidator = tagValidator;
    }

    @Override
    public Tag findById(long id) {
        Optional<Tag> optionalTag = repository.queryFirst(new TagByIdSpecification(id));
        return optionalTag.orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<Tag> findAll() {
        return repository.query(new TagAllSpecification());
    }

    @Override
    public Tag add(Tag tag) {
        Map<String, String> validations = tagValidator.validate(tag);
        if (MapUtils.isNotEmpty(validations)) {
            throw new ValidationException(validations);
        }

        if (repository.exists(new TagByNameSpecification(tag.getName()))) {
            throw new EntityAlreadyExistsException();
        }

        return repository.add(tag);
    }

    @Override
    public boolean remove(long id) {
        return repository.remove(id);
    }
}
