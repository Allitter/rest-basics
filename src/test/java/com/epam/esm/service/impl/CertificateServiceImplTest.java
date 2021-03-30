package com.epam.esm.service.impl;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.repository.specification.CertificateByIdSpecification;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CertificateServiceImplTest {

    private static List<Certificate> getTestCertificates() {
        return List.of();
    }


    @Test
    void testFindByIdShouldReturnCertificateWithQueriedIdIfSuchExist() {
        Certificate expected = new Certificate();

        MainRepository<Certificate> repository = Mockito.mock(MainRepository.class);
        MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.queryFirst(Mockito.isA(CertificateByIdSpecification.class))).thenReturn(Optional.of(expected));
        CertificateServiceImpl service = new CertificateServiceImpl(repository, tagRepository);

        Certificate actual = service.findById(1);

        assertSame(expected, actual);
    }

    @Test
    void testFindAllShouldReturnAllCertificates() {
        Certificate expected = new Certificate();

        MainRepository<Certificate> repository = Mockito.mock(MainRepository.class);
        MainRepository<Tag> tagRepository = Mockito.mock(MainRepository.class);
        Mockito.when(repository.queryFirst(Mockito.isA(CertificateByIdSpecification.class))).thenReturn(Optional.of(expected));
        CertificateServiceImpl service = new CertificateServiceImpl(repository, tagRepository);


    }

    @Test
    void findByTagName() {

    }

    @Test
    void findByName() {

    }

    @Test
    void findByDescription() {

    }

    @Test
    void add() {

    }

    @Test
    void update() {

    }

    @Test
    void remove() {

    }

    @Test
    void filter() {

    }
}