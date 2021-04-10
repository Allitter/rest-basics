package com.epam.esm.service.impl;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.repository.specification.impl.*;
import com.epam.esm.service.CertificateQueryObject;
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
    private static final Certificate CERTIFICATE = new Certificate.Builder()
            .setId(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setPrice(PRICE)
            .setDuration(DURATION)
            .setCreateDate(CREATE_DATE)
            .setLastUpdateDate(LAST_UPDATE_DATE)
            .setTags(new ArrayList<>(TAGS))
            .build();

    private static final Certificate SECOND_CERTIFICATE = new Certificate.Builder()
            .setId(2)
            .setName("second name")
            .setDescription("second description")
            .setCreateDate(LocalDate.now().minusDays(1))
            .setTags(List.of(FIRST_TAG))
            .build();


    private final MainRepository<Certificate> certificateRepository = Mockito.mock(MainRepository.class);
    private final MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
    private final CertificateValidator certificateValidator = Mockito.mock(CertificateValidator.class);
    private final CertificateServiceImpl service = new CertificateServiceImpl(certificateRepository, tagRepository, certificateValidator);

    @Test
    void testFindByIdShouldReturnCertificateWithQueriedIdIfSuchExist() {
        Mockito.when(certificateRepository.queryFirst(Mockito.isA(CertificateByIdSpecification.class))).thenReturn(Optional.of(CERTIFICATE));

        Certificate actual = service.findById(ID);

        assertEquals(CERTIFICATE, actual);
    }

    @Test
    void testFindAllShouldReturnAllCertificates() {
        List<Certificate> certificates = Collections.emptyList();
        Mockito.when(certificateRepository.query(Mockito.isA(CertificateAllSpecification.class))).thenReturn(certificates);

        List<Certificate> actual = service.findAll();

        assertEquals(certificates, actual);
    }

    @Test
    void findByTagNameShouldReturnCertificateStreamWithSimilarTagNames() {
        List<Certificate> certificates = Collections.emptyList();
        Mockito.when(certificateRepository.query(Mockito.isA(CertificateByTagNameSpecification.class))).thenReturn(certificates);

        List<Certificate> actual = service.findByTagName(ANY_TEXT);

        assertEquals(certificates, actual);
    }

    @Test
    void findByNameShouldReturnCertificateStreamWithSimilarName() {
        List<Certificate> certificates = Collections.emptyList();
        Mockito.when(certificateRepository.query(Mockito.isA(CertificateByNameSpecification.class))).thenReturn(certificates);

        List<Certificate> actual = service.findByName(ANY_TEXT);

        assertEquals(certificates, actual);
    }

    @Test
    void findByDescriptionShouldReturnCertificateStreamWithSimilarDescription() {
        List<Certificate> certificates = Collections.emptyList();
        Mockito.when(certificateRepository.query(Mockito.isA(CertificateByDescriptionSpecification.class))).thenReturn(certificates);

        List<Certificate> actual = service.findByDescription(ANY_TEXT);

        assertEquals(certificates, actual);
    }

    @Test
    void testAddShouldAddCertificateToRepository() {
        Mockito.when(certificateRepository.add(Mockito.eq(CERTIFICATE))).thenReturn(CERTIFICATE);
        Mockito.when(certificateValidator.validateForCreate(CERTIFICATE)).thenReturn(Collections.emptyMap());
        Mockito.when(tagRepository.queryFirst(Mockito.isA(TagByNameSpecification.class))).thenReturn(Optional.empty());
        doAnswer(AdditionalAnswers.returnsFirstArg()).when(tagRepository).add(any());

        Certificate actual = service.add(CERTIFICATE);

        assertEquals(CERTIFICATE, actual);
    }

    @Test
    void TestUpdateShouldUpdateCertificate() {
        int newPrice = 1000;
        Certificate certificate = new Certificate.Builder(CERTIFICATE).setLastUpdateDate(LocalDate.now()).setPrice(newPrice).build();
        Mockito.when(certificateValidator.validateForCreate(certificate)).thenReturn(Collections.emptyMap());
        Mockito.when(certificateRepository.update(Mockito.eq(certificate))).thenReturn(certificate);
        Mockito.when(certificateRepository.queryFirst(Mockito.isA(CertificateByIdSpecification.class))).thenReturn(Optional.of(CERTIFICATE));
        Mockito.when(tagRepository.queryFirst(Mockito.isA(TagByNameSpecification.class))).thenReturn(Optional.empty());
        doAnswer(AdditionalAnswers.returnsFirstArg()).when(tagRepository).add(any());

        Certificate actual = service.update(certificate);

        assertEquals(certificate, actual);
    }

    @Test
    void TestUpdateShouldThrowExceptionIfCertificateNotPresent() {
        Mockito.when(certificateValidator.validateForCreate(Mockito.any())).thenReturn(Collections.emptyMap());
        Mockito.when(certificateRepository.queryFirst(Mockito.isA(CertificateByIdSpecification.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(CERTIFICATE));
    }

    @Test
    void testRemoveShouldReturnTrueIfRemoved() {
        Mockito.when(certificateRepository.remove(ID)).thenReturn(true);

        assertTrue(service.remove(ID));
    }

    @Test
    void testRemoveShouldReturnFalseIfNotRemoved() {
        Mockito.when(certificateRepository.remove(ID)).thenReturn(false);

        assertFalse(service.remove(ID));
    }

    @Test
    void testFindCertificatesByQueryObjectShouldFindCertificatesByNameIfSet() {
        Mockito.when(certificateRepository.query(Mockito.anyList())).thenReturn(List.of(CERTIFICATE));
        CertificateQueryObject queryObject = new CertificateQueryObject(NAME, null, null, null, null);
        List<Certificate> expected = List.of(CERTIFICATE);

        List<Certificate> actual = service.findCertificatesByQueryObject(queryObject);

        assertEquals(expected, actual);
    }

    @Test
    void testFindCertificatesByQueryObjectShouldFindCertificatesByDescriptionIfSet() {
        Mockito.when(certificateRepository.query(Mockito.anyList())).thenReturn(List.of(CERTIFICATE));
        CertificateQueryObject queryObject = new CertificateQueryObject(null, null, DESCRIPTION, null, null);
        List<Certificate> expected = List.of(CERTIFICATE);

        List<Certificate> actual = service.findCertificatesByQueryObject(queryObject);

        assertEquals(expected, actual);
    }

    @Test
    void testFindCertificatesByQueryObjectShouldFindCertificatesByTagNameIfSet() {
        Mockito.when(certificateRepository.query(Mockito.anyList())).thenReturn(List.of(CERTIFICATE));
        CertificateQueryObject queryObject = new CertificateQueryObject(null, FIRST_TAG.getName(), null, null, null);
        List<Certificate> expected = List.of(CERTIFICATE);

        List<Certificate> actual = service.findCertificatesByQueryObject(queryObject);

        assertEquals(expected, actual);
    }

    @Test
    void testFindCertificatesByQueryObjectShouldFindAllCertificatesIfNoParametersSet() {
        Mockito.when(certificateRepository.query(Mockito.anyList())).thenReturn(List.of(CERTIFICATE));
        CertificateQueryObject queryObject = new CertificateQueryObject(null, null, null, null, null);
        List<Certificate> expected = List.of(CERTIFICATE);

        List<Certificate> actual = service.findCertificatesByQueryObject(queryObject);

        assertEquals(expected, actual);
    }

    @Test
    void testFindCertificatesByQueryObjectShouldFindCertificatesByMultipleParametersIfSet() {
        Mockito.when(certificateRepository.query(Mockito.anyList())).thenReturn(List.of(CERTIFICATE));
        CertificateQueryObject queryObject = new CertificateQueryObject(NAME, FIRST_TAG.getName(), DESCRIPTION, null, null);
        List<Certificate> expected = List.of(CERTIFICATE);

        List<Certificate> actual = service.findCertificatesByQueryObject(queryObject);

        assertEquals(expected, actual);
    }

    @Test
    void testFindCertificatesByQueryObjectShouldSortCertificatesByByNameAscendingIfAscSet() {
        Mockito.when(certificateRepository.query(Mockito.anyList()))
                .thenReturn(List.of(SECOND_CERTIFICATE, CERTIFICATE));

        CertificateQueryObject queryObject = new CertificateQueryObject(null, null, null, "asc", null);
        List<Certificate> expected = List.of(CERTIFICATE, SECOND_CERTIFICATE);

        List<Certificate> actual = service.findCertificatesByQueryObject(queryObject);

        assertEquals(expected, actual);
    }

    @Test
    void testFindCertificatesByQueryObjectShouldSortCertificatesByByNameDescendingIfDescSet() {
        Mockito.when(certificateRepository.query(Mockito.anyList()))
                .thenReturn(List.of(CERTIFICATE, SECOND_CERTIFICATE));

        CertificateQueryObject queryObject = new CertificateQueryObject(null, null, null, "desc", null);
        List<Certificate> expected = List.of(SECOND_CERTIFICATE, CERTIFICATE);

        List<Certificate> actual = service.findCertificatesByQueryObject(queryObject);

        assertEquals(expected, actual);
    }

    @Test
    void testFindCertificatesByQueryObjectShouldSortCertificatesByByCreateDateAscendingIfAscSet() {
        Mockito.when(certificateRepository.query(Mockito.anyList()))
                .thenReturn(List.of(SECOND_CERTIFICATE, CERTIFICATE));

        CertificateQueryObject queryObject = new CertificateQueryObject(null, null, null, null, "asc");
        List<Certificate> expected = List.of(SECOND_CERTIFICATE, CERTIFICATE);

        List<Certificate> actual = service.findCertificatesByQueryObject(queryObject);

        assertEquals(expected, actual);
    }

    @Test
    void testFindCertificatesByQueryObjectShouldSortCertificatesByByCreateDateDescendingIfDescSet() {
        Mockito.when(certificateRepository.query(Mockito.anyList()))
                .thenReturn(List.of(SECOND_CERTIFICATE, CERTIFICATE));

        CertificateQueryObject queryObject = new CertificateQueryObject(null, null, null, null, "desc");
        List<Certificate> expected = List.of(CERTIFICATE, SECOND_CERTIFICATE);

        List<Certificate> actual = service.findCertificatesByQueryObject(queryObject);

        assertEquals(expected, actual);
    }
}