package com.epam.esm.repository;

import com.epam.esm.config.TestConfig;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.specification.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Component
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CertificateRepositoryTest {
    private static final int FIRST_TAG_INDEX = 0;
    private static final int THIRD_TAG_INDEX = 2;
    private static final int FOURTH_TAG_INDEX = 3;
    private static final int SECOND_TAG_INDEX = 1;
    private static final Certificate FIFTH_CERTIFICATE = new Certificate(5, "fifth certificate", "detailed description for fifth certificate", 200, 730, LocalDate.of(2020, 12, 21), LocalDate.of(2020, 12, 31));
    private static final List<Tag> tags = List.of(
            new Tag(1, "first tag"),
            new Tag(2, "second tag"),
            new Tag(3, "third tag"),
            new Tag(4, "fourth tag")
    );
    private static final List<Certificate> certificates = List.of(
            new Certificate(1, "first certificate", "detailed description for first certificate", 200, 365, LocalDate.of(2021, 1, 21), LocalDate.of(2021, 2, 21),
                    tags.get(FIRST_TAG_INDEX),
                    tags.get(THIRD_TAG_INDEX),
                    tags.get(FOURTH_TAG_INDEX)
            ),
            new Certificate(2, "second certificate", "detailed description for second certificate", 150, 365, LocalDate.of(2021, 2, 21), LocalDate.of(2021, 3, 21),
                    tags.get(SECOND_TAG_INDEX)
            ),
            new Certificate(3, "third certificate", "detailed description for third certificate", 80, 365, LocalDate.of(2021, 1, 21), LocalDate.of(2021, 2, 21),
                    tags.get(SECOND_TAG_INDEX),
                    tags.get(THIRD_TAG_INDEX)
            ),
            new Certificate(4, "fourth certificate", "detailed description for fourth certificate", 200, 730, LocalDate.of(2020, 12, 21), LocalDate.of(2020, 12, 31),
                    tags.get(FIRST_TAG_INDEX),
                    tags.get(THIRD_TAG_INDEX)
            )
    );


    @Qualifier("certificateRepository")
    @Autowired
    private CertificateRepository certificateRepository;

    public CertificateRepositoryTest() {
    }

    public static Object[][] queries() {
        return new Object[][]{
                {new CertificateAllSpecification(), certificates},
                {new CertificateByNameSpecification("third"), List.of(certificates.get(2))},
                {new CertificateByDescriptionSpecification("fourth"), List.of(certificates.get(3))},
                {new CertificateByTagNameSpecification("second"), List.of(certificates.get(1), certificates.get(2))},
                {new CertificateByIdSpecification(1), List.of(certificates.get(0))},
        };
    }

    @Test
    @Order(1)
    void testAddShouldAddCertificateToDataSourceIfCertificateNotYetCreated() {
        Certificate expected =FIFTH_CERTIFICATE;

        Certificate actual = certificateRepository.add(expected);

        assertEquals(expected, actual);
    }

    @Test
    @Order(2)
    void testUpdateShouldUpdateCertificateTIfCertificateIsAlreadyExist() {
        Certificate expected = FIFTH_CERTIFICATE;
        expected.setDescription("new description");

        Certificate actual = certificateRepository.update(expected);

        assertEquals(expected, actual);
    }

    @Test
    @Order(3)
    void testRemoveShouldReturnTrueIfDeletedCertificateFromDataSourceIfExists() {
        assertTrue(certificateRepository.remove(5));
    }

    @Test
    @Order(4)
    void testRemoveShouldReturnFalseIfNotDeletedCertificateFromDataSourceIfExists() {
        assertFalse(certificateRepository.remove(-1));
    }

    @ParameterizedTest
    @MethodSource("queries")
    @Order(6)
    void testQueryShouldReturnListOfCertificatesMatchingTheSpecification(Specification<Certificate> specification, List<Certificate> expected) {
        List<Certificate> actual = certificateRepository.query(specification);

        assertEquals(expected, actual);
    }

    @Test
    @Order(5)
    void testQuerySingleShouldReturnFirstResultForSpecification() {
        Certificate expected = certificates.get(0);

        Certificate actual = certificateRepository.queryFirst(new CertificateByIdSpecification(1)).get();

        assertEquals(expected, actual);
    }
}