package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Tag CRD controller.
 */
@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Fids all tags.
     *
     * @return the list of all tags
     */
    @GetMapping()
    public List<TagDto> findAll() {
        return tagService.findAll();
    }

    /**
     * Find tag by id.
     *
     * @param id the id of tag
     * @return the tag with queried id {@link TagDto}
     */
    @GetMapping(value = "/{id}")
    public TagDto findById(@PathVariable int id) {
        return tagService.findById(id);
    }

    /**
     * Add tag.
     *
     * @param dto the tag to add
     * @return the added tag {@link TagDto}
     */
    @PostMapping()
    public TagDto add(@RequestBody TagDto dto) {
        return tagService.add(dto);
    }

    /**
     * Delete tag.
     *
     * @param id the id of tag to remove
     * @return no content
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity remove(@PathVariable int id) {
        tagService.remove(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
