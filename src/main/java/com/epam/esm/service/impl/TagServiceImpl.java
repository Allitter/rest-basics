package com.epam.esm.service.impl;

import com.epam.esm.controller.dto.TagDto;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.repository.specification.TagAllSpecification;
import com.epam.esm.repository.specification.TagByIdSpecification;
import com.epam.esm.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public List<Tag> findAll() {
        return repository.query(new TagAllSpecification());
    }

    @Override
    public TagDto update(TagDto tag) {
        int id = tag.getId();
        Optional<Tag> optional = repository.queryFirst(new TagByIdSpecification(id));
        if (optional.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Tag old = optional.get();
        String name = tag.getName() == null ? old.getName() : tag.getName();
        Tag updated = new Tag(old.getId(), name);
        updated = repository.update(updated);
        return new TagDto(updated);
    }

    @Override
    public TagDto add(TagDto dto) {
        Tag tag = new Tag(dto.getId(), dto.getName());
        tag = repository.add(tag);
        return new TagDto(tag);
    }

    @Override
    public boolean remove(int id) {
        return repository.remove(id);
    }
}
