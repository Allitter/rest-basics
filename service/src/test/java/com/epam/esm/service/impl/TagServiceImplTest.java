package com.epam.esm.service.impl;

import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.repository.specification.impl.TagAllSpecification;
import com.epam.esm.repository.specification.impl.TagByIdSpecification;
import com.epam.esm.repository.specification.impl.TagByNameSpecification;
import com.epam.esm.validator.TagValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TagServiceImplTest {

    @Test
    void testFindByIdShouldReturnTagDtoWithPassedId() {
        Tag expected = new Tag(1, "name");
        MainRepository<Tag> repository = Mockito.mock(MainRepository.class);
        TagValidator validator = Mockito.mock(TagValidator.class);
        Mockito.when(repository.queryFirst(Mockito.isA(TagByIdSpecification.class))).thenReturn(Optional.of(expected));
        TagServiceImpl service = new TagServiceImpl(repository, validator);

        Tag actual = service.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void testFindByIdShouldThrowExceptionIfEntityNotFound() {
        MainRepository<Tag> repository = Mockito.mock(MainRepository.class);
        TagValidator validator = Mockito.mock(TagValidator.class);
        Mockito.when(repository.queryFirst(Mockito.isA(TagByIdSpecification.class))).thenReturn(Optional.empty());
        TagServiceImpl service = new TagServiceImpl(repository, validator);

        assertThrows(EntityNotFoundException.class, () -> service.findById(1));
    }

    @Test
    void testFindAllShouldReturnDtoForAllTags() {
        List<Tag> expected = List.of(
                new Tag(1, "first"),
                new Tag(2, "second")
        );

        MainRepository<Tag> repository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.query(Mockito.isA(TagAllSpecification.class))).thenReturn(expected);
        TagValidator validator = Mockito.mock(TagValidator.class);
        TagServiceImpl service = new TagServiceImpl(repository, validator);

        List<Tag> actual = service.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void testAddShouldAddTagToRepositoryIfNameIsUnique() {
        Tag expected = new Tag(1, "first");
        MainRepository<Tag> repository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.add(Mockito.isA(Tag.class))).thenReturn(expected);
        TagValidator validator = Mockito.mock(TagValidator.class);
        Mockito.when(validator.validate(expected)).thenReturn(Collections.emptyMap());
        TagServiceImpl service = new TagServiceImpl(repository, validator);

        Tag actual = service.add(expected);

        Mockito.verify(repository, Mockito.times(1)).add(Mockito.eq(expected));
        assertEquals(expected, actual);
    }

    @Test
    void testAddShouldThrowExceptionIfNameIsUsed() {
        Tag tag = new Tag(1, "first");
        MainRepository<Tag> repository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.exists(Mockito.isA(TagByNameSpecification.class))).thenReturn(true);
        TagValidator validator = Mockito.mock(TagValidator.class);
        TagServiceImpl service = new TagServiceImpl(repository, validator);

        assertThrows(EntityAlreadyExistsException.class, () -> service.add(tag));
    }

    @Test
    void testRemoveShouldReturnTrueWhenTagIsRemoved() {
        MainRepository<Tag> repository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.remove(1)).thenReturn(true);
        TagValidator validator = Mockito.mock(TagValidator.class);
        TagServiceImpl service = new TagServiceImpl(repository, validator);

        assertTrue(service.remove(1));
        Mockito.verify(repository, Mockito.times(1)).remove(Mockito.eq(1));
    }

    @Test
    void testRemoveShouldReturnFalseIfTagNotRemoved() {
        MainRepository<Tag> repository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.remove(1)).thenReturn(false);
        TagValidator validator = Mockito.mock(TagValidator.class);
        TagServiceImpl service = new TagServiceImpl(repository, validator);

        assertFalse(service.remove(1));
        Mockito.verify(repository, Mockito.times(1)).remove(Mockito.eq(1));
    }
}