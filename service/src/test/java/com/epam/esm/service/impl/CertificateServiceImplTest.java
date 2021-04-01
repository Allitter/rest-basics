package com.epam.esm.service.impl;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.repository.specification.*;
import com.epam.esm.service.stream.CertificateStream;
import com.epam.esm.service.stream.impl.CertificateStreamImpl;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

class CertificateServiceImplTest {
    private static final String ANY_TEXT = "text";
    private static final int ID = 1;
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final int PRICE = 100;
    private static final int DURATION = 100;
    private static final LocalDate CREATE_DATE = LocalDate.now();
    private static final LocalDate LAST_UPDATE_DATE = LocalDate.now();
    private static final Tag FIRST_TAG = new Tag(1, "tag");
    private static final Tag SECOND_TAG = new Tag(2, "another tag");
    private static final List<Tag> TAGS = List.of(FIRST_TAG, SECOND_TAG);
    private static final Certificate CERTIFICATE
            = new Certificate.Builder()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setPrice(PRICE)
            .setDuration(DURATION)
            .setCreateDate(CREATE_DATE)
            .setLastUpdateDate(LAST_UPDATE_DATE)
            .setTags(new ArrayList<>(TAGS))
            .build();

    public static CertificateDto getDtoEquivalentToCertificate() {
        return new CertificateDto(ID, NAME, DESCRIPTION, PRICE, DURATION, CREATE_DATE, LAST_UPDATE_DATE, new ArrayList<>(TAGS));
    }

    @Test
    void testFindByIdShouldReturnCertificateWithQueriedIdIfSuchExist() {
        MainRepository<Certificate> repository = Mockito.mock(MainRepository.class);
        MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.queryFirst(Mockito.isA(CertificateByIdSpecification.class))).thenReturn(Optional.of(CERTIFICATE));
        CertificateServiceImpl service = new CertificateServiceImpl(repository, tagRepository);
        CertificateDto expected = getDtoEquivalentToCertificate();

        CertificateDto actual = service.findById(ID);

        assertEquals(expected, actual);
    }

    @Test
    void testFindAllShouldReturnAllCertificates() {
        List<Certificate> certificates = Collections.emptyList();
        MainRepository<Certificate> repository = Mockito.mock(MainRepository.class);
        MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.query(Mockito.isA(CertificateAllSpecification.class))).thenReturn(certificates);
        CertificateServiceImpl service = new CertificateServiceImpl(repository, tagRepository);
        CertificateStream expected = new CertificateStreamImpl(certificates);

        CertificateStream actual = service.findAll();

        assertEquals(expected.get(), actual.get());
    }

    @Test
    void findByTagNameShouldReturnCertificateStreamWithSimilarTagNames() {
        List<Certificate> certificates = Collections.emptyList();
        MainRepository<Certificate> repository = Mockito.mock(MainRepository.class);
        MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.query(Mockito.isA(CertificateByTagNameSpecification.class))).thenReturn(certificates);
        CertificateServiceImpl service = new CertificateServiceImpl(repository, tagRepository);
        CertificateStream expected = new CertificateStreamImpl(certificates);

        CertificateStream actual = service.findByTagName(ANY_TEXT);

        assertEquals(expected.get(), actual.get());
    }

    @Test
    void findByNameShouldReturnCertificateStreamWithSimilarName() {
        List<Certificate> certificates = Collections.emptyList();
        MainRepository<Certificate> repository = Mockito.mock(MainRepository.class);
        MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.query(Mockito.isA(CertificateByNameSpecification.class))).thenReturn(certificates);
        CertificateServiceImpl service = new CertificateServiceImpl(repository, tagRepository);
        CertificateStream expected = new CertificateStreamImpl(certificates);

        CertificateStream actual = service.findByName(ANY_TEXT);

        assertEquals(expected.get(), actual.get());
    }

    @Test
    void findByDescriptionShouldReturnCertificateStreamWithSimilarDescription() {
        List<Certificate> certificates = Collections.emptyList();
        MainRepository<Certificate> repository = Mockito.mock(MainRepository.class);
        MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.query(Mockito.isA(CertificateByDescriptionSpecification.class))).thenReturn(certificates);
        CertificateServiceImpl service = new CertificateServiceImpl(repository, tagRepository);
        CertificateStream expected = new CertificateStreamImpl(certificates);

        CertificateStream actual = service.findByDescription(ANY_TEXT);

        assertEquals(expected.get(), actual.get());
    }

    @Test
    void testAddShouldAddCertificateToRepository() {
        MainRepository<Certificate> repository = Mockito.mock(MainRepository.class);
        MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.add(Mockito.eq(CERTIFICATE))).thenReturn(CERTIFICATE);
        Mockito.when(tagRepository.queryFirst(Mockito.isA(TagByNameOrIdSpecification.class))).thenReturn(Optional.empty());
        doAnswer(AdditionalAnswers.returnsFirstArg()).when(tagRepository).add(any());
        CertificateServiceImpl service = new CertificateServiceImpl(repository, tagRepository);
        CertificateDto expected = getDtoEquivalentToCertificate();

        CertificateDto actual = service.add(expected);

        assertEquals(expected, actual);
    }

    @Test
    void TestUpdateShouldUpdateCertificate() {
        CertificateDto dto = getDtoEquivalentToCertificate();
        int newPrice = 1000;
        dto.setPrice(newPrice);
        dto.setName(null);

        Certificate certificate = new Certificate.Builder(CERTIFICATE).setLastUpdateDate(LocalDate.now()).setPrice(newPrice).build();

        MainRepository<Certificate> repository = Mockito.mock(MainRepository.class);
        MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.update(Mockito.eq(certificate))).thenReturn(certificate);
        Mockito.when(repository.queryFirst(Mockito.isA(CertificateByIdSpecification.class))).thenReturn(Optional.of(CERTIFICATE));
        Mockito.when(tagRepository.queryFirst(Mockito.isA(TagByNameOrIdSpecification.class))).thenReturn(Optional.empty());
        doAnswer(AdditionalAnswers.returnsFirstArg()).when(tagRepository).add(any());
        CertificateServiceImpl service = new CertificateServiceImpl(repository, tagRepository);
        CertificateDto expected = getDtoEquivalentToCertificate();
        expected.setPrice(newPrice);

        CertificateDto actual = service.update(dto);

        assertEquals(expected, actual);
    }

    @Test
    void TestUpdateShouldThrowExceptionIfCertificateNotPresent() {
        MainRepository<Certificate> repository = Mockito.mock(MainRepository.class);
        MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.queryFirst(Mockito.isA(CertificateByIdSpecification.class))).thenReturn(Optional.empty());
        CertificateServiceImpl service = new CertificateServiceImpl(repository, tagRepository);
        CertificateDto dto = getDtoEquivalentToCertificate();

        assertThrows(EntityNotFoundException.class, () -> service.update(dto));
    }

    @Test
    void testRemoveShouldReturnTrueIfRemoved() {
        MainRepository<Certificate> repository = Mockito.mock(MainRepository.class);
        MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.remove(ID)).thenReturn(true);
        CertificateServiceImpl service = new CertificateServiceImpl(repository, tagRepository);

        assertTrue(service.remove(ID));
    }

    @Test
    void testRemoveShouldReturnFalseIfNotRemoved() {
        MainRepository<Certificate> repository = Mockito.mock(MainRepository.class);
        MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.remove(ID)).thenReturn(false);
        CertificateServiceImpl service = new CertificateServiceImpl(repository, tagRepository);

        assertFalse(service.remove(ID));
    }
}