package com.epam.esm.repository;

import com.epam.esm.config.TestConfig;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.specification.Specification;
import com.epam.esm.repository.specification.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Component
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CertificateRepositoryTest {
    private static final int FIRST_TAG_INDEX = 0;
    private static final int SECOND_TAG_INDEX = 1;
    private static final int THIRD_TAG_INDEX = 2;
    private static final int FOURTH_TAG_INDEX = 3;
    private static final int FIRST_ELEMENT = 0;
    private static final int NON_EXISTING_ID = -1;
    private static final int FIRST_ELEMENT_ID = 1;
    private static final int SECOND_ELEMENT = 1;
    private static final int THIRD_ELEMENT = 2;
    private static final int FOURTH_ELEMENT = 3;

    private static final List<Tag> TAGS = List.of(
            new Tag(1, "first tag"),
            new Tag(2, "second tag"),
            new Tag(3, "third tag"),
            new Tag(4, "fourth tag")
    );
    private static final List<Certificate> CERTIFICATES = List.of(
            new Certificate.Builder()
                    .setId(FIRST_ELEMENT_ID).setName("first certificate")
                    .setDescription("detailed description for first certificate")
                    .setPrice(200).setDuration(365)
                    .setCreateDate(LocalDate.of(2021, 1, 21))
                    .setLastUpdateDate(LocalDate.of(2021, 2, 21))
                    .setTags(List.of(
                            TAGS.get(FIRST_TAG_INDEX),
                            TAGS.get(THIRD_TAG_INDEX),
                            TAGS.get(FOURTH_TAG_INDEX)))
                    .build(),

            new Certificate.Builder()
                    .setId(2).setName("second certificate")
                    .setDescription("detailed description for second certificate")
                    .setPrice(150).setDuration(365)
                    .setCreateDate(LocalDate.of(2021, 2, 21))
                    .setLastUpdateDate(LocalDate.of(2021, 3, 21))
                    .setTags(List.of(TAGS.get(SECOND_TAG_INDEX)))
                    .build(),

            new Certificate.Builder()
                    .setId(3).setName("third certificate")
                    .setDescription("detailed description for third certificate")
                    .setPrice(80).setDuration(365)
                    .setCreateDate(LocalDate.of(2021, 1, 21))
                    .setLastUpdateDate(LocalDate.of(2021, 2, 21))
                    .setTags(List.of(
                            TAGS.get(SECOND_TAG_INDEX),
                            TAGS.get(THIRD_TAG_INDEX)))
                    .build(),

            new Certificate.Builder()
                    .setId(4).setName("fourth certificate")
                    .setDescription("detailed description for fourth certificate")
                    .setPrice(200).setDuration(730)
                    .setCreateDate(LocalDate.of(2020, 12, 21))
                    .setLastUpdateDate(LocalDate.of(2020, 12, 31))
                    .setTags(List.of(
                            TAGS.get(FIRST_TAG_INDEX),
                            TAGS.get(THIRD_TAG_INDEX)))
                    .build()
    );


    @Qualifier("certificateRepository")
    @Autowired
    private CertificateRepository certificateRepository;

    public CertificateRepositoryTest() {
    }

    public static Object[][] queries() {
        return new Object[][]{
                {new CertificateAllSpecification(), CERTIFICATES},
                {new CertificateByNameSpecification("third"), List.of(CERTIFICATES.get(THIRD_ELEMENT))},
                {new CertificateByDescriptionSpecification("fourth"), List.of(CERTIFICATES.get(FOURTH_ELEMENT))},
                {new CertificateByTagNameSpecification("second"), List.of(CERTIFICATES.get(SECOND_ELEMENT), CERTIFICATES.get(THIRD_ELEMENT))},
                {new CertificateByIdSpecification(FIRST_ELEMENT_ID), List.of(CERTIFICATES.get(FIRST_ELEMENT))},
        };
    }

    @Test
    void testAddShouldAddCertificateToDataSourceIfCertificateNotYetCreated() {
        Certificate expected = new Certificate.Builder()
                .setId(5).setName("fifth certificate")
                .setDescription("detailed description for fifth certificate")
                .setPrice(200).setDuration(730)
                .setCreateDate(LocalDate.of(2020, 12, 21))
                .setLastUpdateDate(LocalDate.of(2020, 12, 31)).build();

        Certificate actual = certificateRepository.add(expected);

        assertEquals(expected, actual);
    }

    @Test
    void testUpdateShouldUpdateCertificateTIfCertificateIsAlreadyExist() {
        Certificate expected = CERTIFICATES.get(FIRST_ELEMENT);
        String newDescription = "new description";
        expected = new Certificate.Builder(expected)
                .setDescription(newDescription).build();

        Certificate actual = certificateRepository.update(expected);

        assertEquals(expected, actual);
    }

    @Test
    void testRemoveShouldReturnTrueIfDeletedCertificateFromDataSourceIfExists() {
        assertTrue(certificateRepository.remove(FIRST_ELEMENT_ID));
    }

    @Test
    void testRemoveShouldReturnFalseIfNotDeletedCertificateFromDataSourceIfExists() {
        assertFalse(certificateRepository.remove(NON_EXISTING_ID));
    }

    @ParameterizedTest
    @MethodSource("queries")
    void testQueryShouldReturnListOfCertificatesMatchingTheSpecification(Specification<Certificate> specification, List<Certificate> expected) {
        List<Certificate> actual = certificateRepository.query(specification);

        assertEquals(expected, actual);
    }

    @Test
    void testQuerySingleShouldReturnFirstResultForSpecification() {
        Certificate expected = CERTIFICATES.get(FIRST_ELEMENT);

        Certificate actual = certificateRepository.queryFirst(new CertificateByIdSpecification(FIRST_ELEMENT_ID)).get();

        assertEquals(expected, actual);
    }
}