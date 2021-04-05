package com.epam.esm.service.stream;

import com.epam.esm.model.Certificate;

/**
 * The Certificate stream interface.
 */
public interface CertificateStream extends ServiceStream<Certificate> {

    /**
     * Filters by certificate name.
     *
     * @param name the name of certificate
     * @return the certificate stream
     */
    CertificateStream nameLike(String name);

    /**
     * Filters by certificate description.
     *
     * @param description the part of description
     * @return the certificate stream
     */
    CertificateStream descriptionLike(String description);

    /**
     * Filters by certificate's tag names.
     *
     * @param tagName the tag name
     * @return the certificate stream
     */
    CertificateStream tagNameLike(String tagName);

    /**
     * Sorts by certificate name.
     *
     * @param asc true if sort ascending, false otherwise
     * @return the certificate stream
     */
    CertificateStream sortName(boolean asc);

    /**
     * Sorts by certificate's create date.
     *
     * @param asc true if sort ascending, false otherwise
     * @return the certificate stream
     */
    CertificateStream sortCreateDate(boolean asc);
}
