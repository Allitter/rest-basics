package com.epam.esm.service.impl;

import com.epam.esm.controller.dto.TagDto;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.repository.specification.TagAllSpecification;
import com.epam.esm.repository.specification.TagByIdSpecification;
import com.epam.esm.repository.specification.TagByNameSpecification;
import com.epam.esm.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    private final MainRepository<Tag> repository;

    public TagServiceImpl(MainRepository<Tag> repository) {
        this.repository = repository;
    }

    @Override
    public TagDto findById(int id) {
        Tag tag = repository.queryFirst(new TagByIdSpecification(id)).orElseThrow(EntityNotFoundException::new);
        return new TagDto(tag);
    }

    @Override
    public List<TagDto> findAll() {
        List<Tag> tags = repository.query(new TagAllSpecification());
        return tags.stream().map(TagDto::new).collect(Collectors.toList());
    }

    @Override
    public TagDto add(TagDto dto) {
        Optional<Tag> tagByName = repository.queryFirst(new TagByNameSpecification(dto.getName()));
        if (tagByName.isPresent()) {
            throw new EntityAlreadyExistsException();
        }

        Tag tag = new Tag(dto.getId(), dto.getName());
        tag = repository.add(tag);
        return new TagDto(tag);
    }

    @Override
    public boolean remove(int id) {
        return repository.remove(id);
    }
}
