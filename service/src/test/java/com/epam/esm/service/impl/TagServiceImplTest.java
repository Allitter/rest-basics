package com.epam.esm.service.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.repository.specification.TagAllSpecification;
import com.epam.esm.repository.specification.TagByIdSpecification;
import com.epam.esm.repository.specification.TagByNameSpecification;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TagServiceImplTest {

    @Test
    void testFindByIdShouldReturnTagDtoWithPassedId() {
        Tag tag = new Tag(1, "name");
        MainRepository<Tag> repository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.queryFirst(Mockito.isA(TagByIdSpecification.class))).thenReturn(Optional.of(tag));
        TagServiceImpl service = new TagServiceImpl(repository);
        TagDto expected = new TagDto(1, "name");

        TagDto actual = service.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void testFindByIdShouldThrowExceptionIfEntityNotFound() {
        MainRepository<Tag> repository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.queryFirst(Mockito.isA(TagByIdSpecification.class))).thenReturn(Optional.empty());
        TagServiceImpl service = new TagServiceImpl(repository);

        assertThrows(EntityNotFoundException.class, () -> service.findById(1));
    }

    @Test
    void testFindAllShouldReturnDtoForAllTags() {
        List<Tag> tags = List.of(
                new Tag(1, "first"),
                new Tag(2, "second")
        );
        List<TagDto> expected = List.of(
                new TagDto(1, "first"),
                new TagDto(2, "second")
        );
        MainRepository<Tag> repository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.query(Mockito.isA(TagAllSpecification.class))).thenReturn(tags);
        TagServiceImpl service = new TagServiceImpl(repository);

        List<TagDto> actual = service.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void testAddShouldAddTagToRepositoryIfNameIsUnique() {
        Tag tag = new Tag(1, "first");
        MainRepository<Tag> repository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.add(Mockito.isA(Tag.class))).thenReturn(tag);
        TagServiceImpl service = new TagServiceImpl(repository);
        TagDto expected = new TagDto(1, "first");

        TagDto actual = service.add(expected);

        Mockito.verify(repository, Mockito.times(1)).add(Mockito.eq(tag));
        assertEquals(expected, actual);
    }

    @Test
    void testAddShouldThrowExceptionIfNameIsUsed() {
        Tag tag = new Tag(1, "first");
        MainRepository<Tag> repository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.queryFirst(Mockito.isA(TagByNameSpecification.class))).thenReturn(Optional.of(tag));
        TagServiceImpl service = new TagServiceImpl(repository);
        TagDto dto = new TagDto(1, "first");

        assertThrows(EntityAlreadyExistsException.class, () -> service.add(dto));
    }

    @Test
    void testRemoveShouldReturnTrueWhenTagIsRemoved() {
        MainRepository<Tag> repository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.remove(1)).thenReturn(true);
        TagServiceImpl service = new TagServiceImpl(repository);

        assertTrue(service.remove(1));
        Mockito.verify(repository, Mockito.times(1)).remove(Mockito.eq(1));
    }

    @Test
    void testRemoveShouldReturnFalseIfTagNotRemoved() {
        MainRepository<Tag> repository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.remove(1)).thenReturn(false);
        TagServiceImpl service = new TagServiceImpl(repository);

        assertFalse(service.remove(1));
        Mockito.verify(repository, Mockito.times(1)).remove(Mockito.eq(1));
    }
}