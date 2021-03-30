package com.epam.esm.controller;

import com.epam.esm.controller.dto.TagDto;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping()
    public List<Tag> findTags() {
        return tagService.findAll();
    }

    @GetMapping(value = "/{id}")
    public TagDto find(@PathVariable int id) {
        return tagService.findById(id);
    }

    @PostMapping()
    public TagDto add(@RequestParam TagDto dto) {
        return tagService.add(dto);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id) {
        tagService.remove(id);
    }
}
