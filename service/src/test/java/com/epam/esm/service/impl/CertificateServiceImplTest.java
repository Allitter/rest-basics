package com.epam.esm.service.impl;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.repository.specification.impl.*;
import com.epam.esm.validator.CertificateValidator;
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

    @Test
    void testFindByIdShouldReturnCertificateWithQueriedIdIfSuchExist() {
        MainRepository<Certificate> repository = Mockito.mock(MainRepository.class);
        MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
        CertificateValidator validator = Mockito.mock(CertificateValidator.class);
        Mockito.when(repository.queryFirst(Mockito.isA(CertificateByIdSpecification.class))).thenReturn(Optional.of(CERTIFICATE));
        CertificateServiceImpl service = new CertificateServiceImpl(repository, tagRepository, validator);

        Certificate actual = service.findById(ID);

        assertEquals(CERTIFICATE, actual);
    }

    @Test
    void testFindAllShouldReturnAllCertificates() {
        List<Certificate> certificates = Collections.emptyList();
        MainRepository<Certificate> repository = Mockito.mock(MainRepository.class);
        MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
        CertificateValidator validator = Mockito.mock(CertificateValidator.class);
        Mockito.when(repository.query(Mockito.isA(CertificateAllSpecification.class))).thenReturn(certificates);
        CertificateServiceImpl service = new CertificateServiceImpl(repository, tagRepository, validator);

        List<Certificate> actual = service.findAll();

        assertEquals(certificates, actual);
    }

    @Test
    void findByTagNameShouldReturnCertificateStreamWithSimilarTagNames() {
        List<Certificate> certificates = Collections.emptyList();
        MainRepository<Certificate> repository = Mockito.mock(MainRepository.class);
        MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
        CertificateValidator validator = Mockito.mock(CertificateValidator.class);
        Mockito.when(repository.query(Mockito.isA(CertificateByTagNameSpecification.class))).thenReturn(certificates);
        CertificateServiceImpl service = new CertificateServiceImpl(repository, tagRepository, validator);

        List<Certificate> actual = service.findByTagName(ANY_TEXT);

        assertEquals(certificates, actual);
    }

    @Test
    void findByNameShouldReturnCertificateStreamWithSimilarName() {
        List<Certificate> certificates = Collections.emptyList();
        MainRepository<Certificate> repository = Mockito.mock(MainRepository.class);
        MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
        CertificateValidator validator = Mockito.mock(CertificateValidator.class);
        Mockito.when(repository.query(Mockito.isA(CertificateByNameSpecification.class))).thenReturn(certificates);
        CertificateServiceImpl service = new CertificateServiceImpl(repository, tagRepository, validator);

        List<Certificate> actual = service.findByName(ANY_TEXT);

        assertEquals(certificates, actual);
    }

    @Test
    void findByDescriptionShouldReturnCertificateStreamWithSimilarDescription() {
        List<Certificate> certificates = Collections.emptyList();
        MainRepository<Certificate> repository = Mockito.mock(MainRepository.class);
        MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
        CertificateValidator validator = Mockito.mock(CertificateValidator.class);
        Mockito.when(repository.query(Mockito.isA(CertificateByDescriptionSpecification.class))).thenReturn(certificates);
        CertificateServiceImpl service = new CertificateServiceImpl(repository, tagRepository, validator);

        List<Certificate> actual = service.findByDescription(ANY_TEXT);

        assertEquals(certificates, actual);
    }

    @Test
    void testAddShouldAddCertificateToRepository() {
        MainRepository<Certificate> repository = Mockito.mock(MainRepository.class);
        MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.add(Mockito.eq(CERTIFICATE))).thenReturn(CERTIFICATE);
        CertificateValidator validator = Mockito.mock(CertificateValidator.class);
        Mockito.when(validator.validateForCreate(CERTIFICATE)).thenReturn(Collections.emptyMap());
        Mockito.when(tagRepository.queryFirst(Mockito.isA(TagByNameSpecification.class))).thenReturn(Optional.empty());
        doAnswer(AdditionalAnswers.returnsFirstArg()).when(tagRepository).add(any());
        CertificateServiceImpl service = new CertificateServiceImpl(repository, tagRepository, validator);

        Certificate actual = service.add(CERTIFICATE);

        assertEquals(CERTIFICATE, actual);
    }

    @Test
    void TestUpdateShouldUpdateCertificate() {
        int newPrice = 1000;
        Certificate certificate = new Certificate.Builder(CERTIFICATE).setLastUpdateDate(LocalDate.now()).setPrice(newPrice).build();

        MainRepository<Certificate> repository = Mockito.mock(MainRepository.class);
        MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
        CertificateValidator validator = Mockito.mock(CertificateValidator.class);
        Mockito.when(validator.validateForCreate(certificate)).thenReturn(Collections.emptyMap());
        Mockito.when(repository.update(Mockito.eq(certificate))).thenReturn(certificate);
        Mockito.when(repository.queryFirst(Mockito.isA(CertificateByIdSpecification.class))).thenReturn(Optional.of(CERTIFICATE));
        Mockito.when(tagRepository.queryFirst(Mockito.isA(TagByNameSpecification.class))).thenReturn(Optional.empty());
        doAnswer(AdditionalAnswers.returnsFirstArg()).when(tagRepository).add(any());
        CertificateServiceImpl service = new CertificateServiceImpl(repository, tagRepository, validator);

        Certificate actual = service.update(certificate);

        assertEquals(certificate, actual);
    }

    @Test
    void TestUpdateShouldThrowExceptionIfCertificateNotPresent() {
        MainRepository<Certificate> repository = Mockito.mock(MainRepository.class);
        MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
        CertificateValidator validator = Mockito.mock(CertificateValidator.class);
        Mockito.when(validator.validateForCreate(Mockito.any())).thenReturn(Collections.emptyMap());
        Mockito.when(repository.queryFirst(Mockito.isA(CertificateByIdSpecification.class))).thenReturn(Optional.empty());
        CertificateServiceImpl service = new CertificateServiceImpl(repository, tagRepository, validator);

        assertThrows(EntityNotFoundException.class, () -> service.update(CERTIFICATE));
    }

    @Test
    void testRemoveShouldReturnTrueIfRemoved() {
        MainRepository<Certificate> repository = Mockito.mock(MainRepository.class);
        MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
        CertificateValidator validator = Mockito.mock(CertificateValidator.class);
        Mockito.when(repository.remove(ID)).thenReturn(true);
        CertificateServiceImpl service = new CertificateServiceImpl(repository, tagRepository, validator);

        assertTrue(service.remove(ID));
    }

    @Test
    void testRemoveShouldReturnFalseIfNotRemoved() {
        MainRepository<Certificate> repository = Mockito.mock(MainRepository.class);
        MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
        CertificateValidator validator = Mockito.mock(CertificateValidator.class);
        Mockito.when(repository.remove(ID)).thenReturn(false);
        CertificateServiceImpl service = new CertificateServiceImpl(repository, tagRepository, validator);

        assertFalse(service.remove(ID));
    }
}