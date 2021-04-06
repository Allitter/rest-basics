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
    private static final long FIRST_TAG_ID = 1;
    private static final String FIRST_TAG_NAME = "first";
    private static final Tag FIRST_TAG = new Tag(FIRST_TAG_ID, FIRST_TAG_NAME);
    private static final long SECOND_TAG_ID = 2;
    private static final String SECOND_TAG_NAME = "second";
    private static final Tag SECOND_TAG = new Tag(SECOND_TAG_ID, SECOND_TAG_NAME);
    private static final long NON_EXISTING_ID = -1;

    @Test
    void testFindByIdShouldReturnTagDtoWithPassedId() {
        MainRepository<Tag> repository = Mockito.mock(MainRepository.class);
        TagValidator validator = Mockito.mock(TagValidator.class);
        Mockito.when(repository.queryFirst(Mockito.isA(TagByIdSpecification.class))).thenReturn(Optional.of(FIRST_TAG));
        TagServiceImpl service = new TagServiceImpl(repository, validator);

        Tag actual = service.findById(FIRST_TAG_ID);

        assertEquals(FIRST_TAG, actual);
    }

    @Test
    void testFindByIdShouldThrowExceptionIfEntityNotFound() {
        MainRepository<Tag> repository = Mockito.mock(MainRepository.class);
        TagValidator validator = Mockito.mock(TagValidator.class);
        Mockito.when(repository.queryFirst(Mockito.isA(TagByIdSpecification.class))).thenReturn(Optional.empty());
        TagServiceImpl service = new TagServiceImpl(repository, validator);

        assertThrows(EntityNotFoundException.class, () -> service.findById(NON_EXISTING_ID));
    }

    @Test
    void testFindAllShouldReturnDtoForAllTags() {
        List<Tag> expected = List.of(
                FIRST_TAG,
                SECOND_TAG
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
        MainRepository<Tag> repository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.add(Mockito.isA(Tag.class))).thenReturn(FIRST_TAG);
        TagValidator validator = Mockito.mock(TagValidator.class);
        Mockito.when(validator.validate(FIRST_TAG)).thenReturn(Collections.emptyMap());
        TagServiceImpl service = new TagServiceImpl(repository, validator);

        Tag actual = service.add(FIRST_TAG);

        Mockito.verify(repository, Mockito.times(1)).add(Mockito.eq(FIRST_TAG));
        assertEquals(FIRST_TAG, actual);
    }

    @Test
    void testAddShouldThrowExceptionIfNameIsUsed() {
        MainRepository<Tag> repository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.exists(Mockito.isA(TagByNameSpecification.class))).thenReturn(true);
        TagValidator validator = Mockito.mock(TagValidator.class);
        TagServiceImpl service = new TagServiceImpl(repository, validator);

        assertThrows(EntityAlreadyExistsException.class, () -> service.add(FIRST_TAG));
    }

    @Test
    void testRemoveShouldReturnTrueWhenTagIsRemoved() {
        MainRepository<Tag> repository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.remove(FIRST_TAG_ID)).thenReturn(true);
        TagValidator validator = Mockito.mock(TagValidator.class);
        TagServiceImpl service = new TagServiceImpl(repository, validator);

        assertTrue(service.remove(FIRST_TAG_ID));
        Mockito.verify(repository, Mockito.times(1)).remove(Mockito.eq(FIRST_TAG_ID));
    }

    @Test
    void testRemoveShouldReturnFalseIfTagNotRemoved() {
        MainRepository<Tag> repository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.remove(FIRST_TAG_ID)).thenReturn(false);
        TagValidator validator = Mockito.mock(TagValidator.class);
        TagServiceImpl service = new TagServiceImpl(repository, validator);

        assertFalse(service.remove(FIRST_TAG_ID));
        Mockito.verify(repository, Mockito.times(1)).remove(Mockito.eq(FIRST_TAG_ID));
    }
}