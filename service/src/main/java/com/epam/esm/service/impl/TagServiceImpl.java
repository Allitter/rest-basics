package com.epam.esm.service.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.repository.specification.TagAllSpecification;
import com.epam.esm.repository.specification.TagByIdSpecification;
import com.epam.esm.repository.specification.TagByNameSpecification;
import com.epam.esm.service.TagService;
import com.epam.esm.util.DtoConverter;
import com.epam.esm.validation.TagDtoValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    private final MainRepository<Tag> repository;
    private final TagDtoValidator tagValidator;

    public TagServiceImpl(MainRepository<Tag> repository, TagDtoValidator tagValidator) {
        this.repository = repository;
        this.tagValidator = tagValidator;
    }

    @Override
    public TagDto findById(int id) {
        Optional<Tag> optionalTag = repository.queryFirst(new TagByIdSpecification(id));
        Tag tag = optionalTag.orElseThrow(EntityNotFoundException::new);
        return DtoConverter.tagToDto(tag);
    }

    @Override
    public List<TagDto> findAll() {
        List<Tag> tags = repository.query(new TagAllSpecification());
        return tags.stream().map(DtoConverter::tagToDto).collect(Collectors.toList());
    }

    @Override
    public TagDto add(TagDto dto) {
        Map<String, String> validations = tagValidator.validate(dto);
        if (!validations.isEmpty()) {
            throw new ValidationException(validations);
        }

        if (repository.exists(new TagByNameSpecification(dto.getName()))) {
            throw new EntityAlreadyExistsException();
        }

        Tag tag = DtoConverter.dtoToTag(dto);
        tag = repository.add(tag);
        return DtoConverter.tagToDto(tag);
    }

    @Override
    public boolean remove(int id) {
        return repository.remove(id);
    }
}
