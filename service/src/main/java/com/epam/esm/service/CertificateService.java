package com.epam.esm.service;

import com.epam.esm.model.Certificate;

import java.util.List;

/**
 * Certificate service interface.
 */
public interface CertificateService {

    /**
     * Finds certificate by id.
     *
     * @param id the id
     * @return {@link Certificate}
     */
    Certificate findById(long id);

    /**
     * Finds all certificates
     *
     * @return all certificates
     */
    List<Certificate> findAll();

    /**
     * Finds certificate by tag name
     *
     * @param tagName the name of tag to search
     * @return certificates that contain tags with part of queried name
     */
    List<Certificate> findByTagName(String tagName);

    /**
     * Find by name certificate stream.
     *
     * @param name part of name to search
     * @return certificates that contain asked part of name
     */
    List<Certificate> findByName(String name);

    /**
     * Find by description certificate stream.
     *
     * @param description part of description to search
     * @return certificates that contain asked part of description
     */
    List<Certificate> findByDescription(String description);

    /**
     * Update certificate dto.
     *
     * @param certificate certificate to update
     * @return the updated certificate
     */
    Certificate update(Certificate certificate);

    /**
     * Add certificate dto.
     *
     * @param certificate certificate to add
     * @return the added certificate
     */
    Certificate add(Certificate certificate);

    /**
     * Remove boolean.
     *
     * @param id of certificate to remove
     * @return true if removed, false otherwise
     */
    boolean remove(long id);

    /**
     * Find certificates by query object list.
     *
     * @param queryObject the query object
     * @return queried certificates
     */
    List<Certificate> findCertificatesByQueryObject(CertificateQueryObject queryObject);
}
