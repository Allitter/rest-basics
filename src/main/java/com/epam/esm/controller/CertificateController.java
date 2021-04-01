package com.epam.esm.controller;

import com.epam.esm.controller.dto.CertificateDto;
import com.epam.esm.controller.dto.CertificateQueryObject;
import com.epam.esm.model.Certificate;
import com.epam.esm.service.CertificateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The controller to provide CRUD operations on {@link Certificate}.
 */
@RestController
@RequestMapping("/certificates")
public class CertificateController {
    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    /**
     * Search for certificates by passed params.
     *
     * @param query Object containing all params
     * @return the list of queried certificates or all certificates if no params passed
     */
    @GetMapping()
    public List<CertificateDto> findByQuery(CertificateQueryObject query) {
        return certificateService.findCertificatesByQueryObject(query);
    }

    /**
     * Find by id.
     *
     * @param id the id of certificate
     * @return the {@link CertificateDto} of queried certificate
     */
    @GetMapping(value = "/{id}")
    public CertificateDto findById(@PathVariable int id) {
        return certificateService.findById(id);
    }

    /**
     * Add certificate.
     *
     * @param dto The certificate to add
     * @return the {@link CertificateDto} of added certificate
     */
    @PostMapping()
    public CertificateDto add(@RequestBody CertificateDto dto) {
        return certificateService.add(dto);
    }

    /**
     * Update certificate.
     *
     * @param id  the id of certificate to update
     * @param dto the updated fields of certificate
     * @return the updated certificate {@link CertificateDto}
     */
    @PutMapping(value = "/{id}")
    public CertificateDto update(@PathVariable int id, @RequestBody CertificateDto dto) {
        dto.setId(id);
        return certificateService.update(dto);
    }

    /**
     * Delete certificate.
     *
     * @param id the id of certificate
     * @return no content
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity remove(@PathVariable int id) {
        certificateService.remove(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
