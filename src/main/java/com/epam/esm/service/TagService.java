package com.epam.esm.service;

import com.epam.esm.controller.dto.TagDto;
import com.epam.esm.model.Tag;

import java.util.List;

public interface TagService {

    TagDto findById(int id);

    List<Tag> findAll();

    TagDto update(TagDto dto);

    TagDto add(TagDto dto);

    boolean remove(int id);

}
